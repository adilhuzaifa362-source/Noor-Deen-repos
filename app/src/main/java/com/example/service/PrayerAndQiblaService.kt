package com.example.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.model.CalculationMethod
import com.example.model.CityLocation
import com.example.model.JuristicMethod
import com.example.model.PrayerTime
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

object PrayerAndQiblaService {

    private const val KAABA_LAT = 21.422487
    private const val KAABA_LON = 39.826206

    /**
     * Calculates the Qibla direction (bearing in degrees 0-360 from true North)
     */
    fun calculateQiblaBearing(userLat: Double, userLon: Double): Double {
        val lat1Rad = Math.toRadians(userLat)
        val lat2Rad = Math.toRadians(KAABA_LAT)
        val deltaLonRad = Math.toRadians(KAABA_LON - userLon)

        val y = sin(deltaLonRad) * cos(lat2Rad)
        val x = cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(deltaLonRad)

        val bearingRad = atan2(y, x)
        val bearingDeg = Math.toDegrees(bearingRad)
        return (bearingDeg + 360.0) % 360.0
    }

    /**
     * Computes the 5 daily prayer times + Sunrise for a given date, coordinates, and calculation method.
     */
    fun computePrayerTimes(
        city: CityLocation,
        calendar: Calendar = Calendar.getInstance(),
        calculationMethod: CalculationMethod = CalculationMethod.MWL,
        juristicMethod: JuristicMethod = JuristicMethod.SHAFI_STANDARD
    ): List<PrayerTime> {
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val lat = city.latitude
        val lon = city.longitude
        val tz = city.timeZoneOffsetHours

        // Approximate solar declination & equation of time
        val b = 2 * PI * (dayOfYear - 81) / 365.0
        val eot = 9.87 * sin(2 * b) - 7.53 * cos(b) - 1.5 * sin(b) // in minutes
        val declinationDeg = 23.45 * sin(2 * PI * (284 + dayOfYear) / 365.0)
        val declinationRad = Math.toRadians(declinationDeg)
        val latRad = Math.toRadians(lat)

        // Solar Noon (Dhuhr)
        val solarNoonHours = 12.0 + tz - (lon / 15.0) - (eot / 60.0)

        // Hour angle helper
        fun hourAngle(angleBelowHorizonDeg: Double): Double? {
            val hRad = Math.toRadians(angleBelowHorizonDeg)
            val num = -sin(hRad) - sin(latRad) * sin(declinationRad)
            val den = cos(latRad) * cos(declinationRad)
            val cosH = num / den
            if (cosH < -1.0 || cosH > 1.0) return null
            return Math.toDegrees(acos(cosH)) / 15.0 // in hours
        }

        // Sunrise & Sunset angle is 0.833°
        val sunRiseHourAngle = hourAngle(0.833) ?: 6.0
        val sunriseHours = solarNoonHours - sunRiseHourAngle
        val maghribHours = solarNoonHours + sunRiseHourAngle

        // Fajr angle
        val fajrHourAngle = hourAngle(calculationMethod.fajrAngle) ?: (sunRiseHourAngle + 1.5)
        val fajrHours = solarNoonHours - fajrHourAngle

        // Isha angle / time
        val ishaHours = if (calculationMethod == CalculationMethod.UMM_AL_QURA) {
            maghribHours + 1.5 // 90 min after Maghrib
        } else {
            val ishaHourAngle = hourAngle(calculationMethod.ishaAngle) ?: (sunRiseHourAngle + 1.5)
            solarNoonHours + ishaHourAngle
        }

        // Asr calculation
        val shadowRatio = if (juristicMethod == JuristicMethod.HANAFI) 2.0 else 1.0
        val phiMinusDelta = Math.abs(latRad - declinationRad)
        val asrAltitudeRad = atan(1.0 / (shadowRatio + tan(phiMinusDelta)))
        val asrNum = sin(asrAltitudeRad) - sin(latRad) * sin(declinationRad)
        val asrDen = cos(latRad) * cos(declinationRad)
        val asrCosH = (asrNum / asrDen).coerceIn(-1.0, 1.0)
        val asrHourAngle = Math.toDegrees(acos(asrCosH)) / 15.0
        val asrHours = solarNoonHours + asrHourAngle

        // Format to string HH:mm
        fun formatTime(hoursFloat: Double): String {
            var totalMin = (hoursFloat * 60).toInt()
            while (totalMin < 0) totalMin += 1440
            totalMin %= 1440
            val h = totalMin / 60
            val m = totalMin % 60
            return String.format("%02d:%02d", h, m)
        }

        val fajrStr = formatTime(fajrHours)
        val sunriseStr = formatTime(sunriseHours)
        val dhuhrStr = formatTime(solarNoonHours)
        val asrStr = formatTime(asrHours)
        val maghribStr = formatTime(maghribHours)
        val ishaStr = formatTime(ishaHours)

        // Find current time and next prayer
        val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

        fun parseMin(s: String): Int {
            val parts = s.split(":")
            return parts[0].toInt() * 60 + parts[1].toInt()
        }

        val prayerMinutes = listOf(
            parseMin(fajrStr),
            parseMin(sunriseStr),
            parseMin(dhuhrStr),
            parseMin(asrStr),
            parseMin(maghribStr),
            parseMin(ishaStr)
        )

        var nextIdx = 0
        for (i in prayerMinutes.indices) {
            if (currentMinutes < prayerMinutes[i]) {
                nextIdx = i
                break
            }
            if (i == prayerMinutes.size - 1) {
                nextIdx = 0 // wraps to Fajr tomorrow
            }
        }

        return listOf(
            PrayerTime("Fajr", "فجر", "फ़ज्र", "الفجر", fajrStr, isNext = nextIdx == 0, isPast = currentMinutes >= prayerMinutes[0]),
            PrayerTime("Sunrise", "طلوعِ آفتاب", "सूर्योदय", "الشروق", sunriseStr, isNext = nextIdx == 1, isPast = currentMinutes >= prayerMinutes[1]),
            PrayerTime("Dhuhr", "ظہر", "ज़ुहर", "الظهر", dhuhrStr, isNext = nextIdx == 2, isPast = currentMinutes >= prayerMinutes[2]),
            PrayerTime("Asr", "عصر", "असर", "العصر", asrStr, isNext = nextIdx == 3, isPast = currentMinutes >= prayerMinutes[3]),
            PrayerTime("Maghrib", "مغرب", "मग्रिब", "المغرب", maghribStr, isNext = nextIdx == 4, isPast = currentMinutes >= prayerMinutes[4]),
            PrayerTime("Isha", "عشاء", "इशा", "العشاء", ishaStr, isNext = nextIdx == 5, isPast = currentMinutes >= prayerMinutes[5])
        )
    }

    /**
     * Sensor Compass Manager for live Qibla pointer
     */
    class CompassSensorManager(
        private val context: Context,
        private val onHeadingChanged: (heading: Float, accuracy: Int) -> Unit
    ) : SensorEventListener {

        private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        private val gravity = FloatArray(3)
        private val geomagnetic = FloatArray(3)
        private val rotationMatrix = FloatArray(9)
        private val orientation = FloatArray(3)

        private var isRegistered = false

        fun startListening() {
            if (isRegistered) return
            if (rotationSensor != null) {
                sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
                isRegistered = true
            } else if (accelerometer != null && magnetometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
                sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
                isRegistered = true
            }
        }

        fun stopListening() {
            if (!isRegistered) return
            sensorManager.unregisterListener(this)
            isRegistered = false
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return

            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val azimuthInDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
                val normalizedHeading = (azimuthInDegrees + 360f) % 360f
                onHeadingChanged(normalizedHeading, event.accuracy)
                return
            }

            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, gravity, 0, 3)
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, geomagnetic, 0, 3)
            }

            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                SensorManager.getOrientation(rotationMatrix, orientation)
                val azimuthInDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
                val normalizedHeading = (azimuthInDegrees + 360f) % 360f
                onHeadingChanged(normalizedHeading, event.accuracy)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handled in onSensorChanged or via accuracy callback
        }
    }
}
