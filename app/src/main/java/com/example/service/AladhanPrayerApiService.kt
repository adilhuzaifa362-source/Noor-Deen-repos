package com.example.service

import com.example.model.CalculationMethod
import com.example.model.JuristicMethod
import com.example.model.PrayerTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.Calendar
import java.util.concurrent.TimeUnit

data class AladhanResult(
    val prayerTimes: List<PrayerTime>,
    val hijriDateFormatted: String,
    val gregorianDateFormatted: String,
    val timezone: String,
    val methodName: String,
    val latitude: Double,
    val longitude: Double
)

object AladhanPrayerApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private const val BASE_URL = "https://api.aladhan.com/v1"

    /**
     * Map our app's CalculationMethod to Aladhan method ID:
     * 1: University of Islamic Sciences, Karachi
     * 2: Islamic Society of North America (ISNA)
     * 3: Muslim World League (MWL)
     * 4: Umm Al-Qura University, Makkah
     * 5: Egyptian General Authority of Survey
     */
    fun mapCalculationMethodId(method: CalculationMethod): Int {
        return when (method) {
            CalculationMethod.KARACHI -> 1
            CalculationMethod.ISNA -> 2
            CalculationMethod.MWL -> 3
            CalculationMethod.UMM_AL_QURA -> 4
            CalculationMethod.EGYPT -> 5
        }
    }

    /**
     * Map JuristicMethod to Aladhan school:
     * 0 = Shafi'i (Standard Shadow 1x)
     * 1 = Hanafi (Shadow 2x)
     */
    fun mapSchoolId(juristicMethod: JuristicMethod): Int {
        return when (juristicMethod) {
            JuristicMethod.SHAFI_STANDARD -> 0
            JuristicMethod.HANAFI -> 1
        }
    }

    /**
     * Fetches daily prayer times based on latitude & longitude from the Aladhan public API.
     */
    suspend fun fetchTimingsByCoordinates(
        latitude: Double,
        longitude: Double,
        calculationMethod: CalculationMethod = CalculationMethod.MWL,
        juristicMethod: JuristicMethod = JuristicMethod.SHAFI_STANDARD
    ): Result<AladhanResult> = withContext(Dispatchers.IO) {
        try {
            val timestamp = System.currentTimeMillis() / 1000
            val methodId = mapCalculationMethodId(calculationMethod)
            val schoolId = mapSchoolId(juristicMethod)

            val url = "$BASE_URL/timings/$timestamp?latitude=$latitude&longitude=$longitude&method=$methodId&school=$schoolId"

            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("User-Agent", "NoorDeen-Android-App")
                .build()

            val response = client.newCall(request).execute()
            val bodyString = response.body?.string()

            if (!response.isSuccessful || bodyString == null) {
                return@withContext Result.failure(Exception("Aladhan API error: HTTP ${response.code}"))
            }

            val json = JSONObject(bodyString)
            val code = json.optInt("code", 0)
            if (code != 200) {
                return@withContext Result.failure(Exception("API returned code $code"))
            }

            val dataObj = json.getJSONObject("data")
            val timingsObj = dataObj.getJSONObject("timings")
            val dateObj = dataObj.getJSONObject("date")
            val metaObj = dataObj.getJSONObject("meta")

            fun cleanTime(raw: String): String {
                // Aladhan sometimes returns "05:14 (EET)" or "05:14"
                return raw.split(" ")[0].trim()
            }

            val fajr = cleanTime(timingsObj.getString("Fajr"))
            val sunrise = cleanTime(timingsObj.getString("Sunrise"))
            val dhuhr = cleanTime(timingsObj.getString("Dhuhr"))
            val asr = cleanTime(timingsObj.getString("Asr"))
            val maghrib = cleanTime(timingsObj.getString("Maghrib"))
            val isha = cleanTime(timingsObj.getString("Isha"))

            // Hijri Date parsing
            val hijriObj = dateObj.optJSONObject("hijri")
            val hijriDay = hijriObj?.optString("day", "") ?: ""
            val hijriMonth = hijriObj?.optJSONObject("month")?.optString("en", "") ?: ""
            val hijriMonthAr = hijriObj?.optJSONObject("month")?.optString("ar", "") ?: ""
            val hijriYear = hijriObj?.optString("year", "") ?: ""
            val hijriFormatted = if (hijriDay.isNotEmpty()) "$hijriDay $hijriMonth ($hijriMonthAr) $hijriYear AH" else ""

            val gregorianObj = dateObj.optJSONObject("gregorian")
            val gregDateFormatted = gregorianObj?.optString("date", "") ?: ""

            val timezone = metaObj.optString("timezone", "")
            val methodName = metaObj.optJSONObject("method")?.optString("name", calculationMethod.displayName) ?: calculationMethod.displayName

            // Calculate next prayer
            val calendar = Calendar.getInstance()
            val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

            fun parseMin(s: String): Int {
                val parts = s.split(":")
                return if (parts.size >= 2) parts[0].toIntOrNull()?.times(60)?.plus(parts[1].toIntOrNull() ?: 0) ?: 0 else 0
            }

            val timesList = listOf(
                "Fajr" to fajr,
                "Sunrise" to sunrise,
                "Dhuhr" to dhuhr,
                "Asr" to asr,
                "Maghrib" to maghrib,
                "Isha" to isha
            )

            var nextIdx = 0
            for (i in timesList.indices) {
                val min = parseMin(timesList[i].second)
                if (currentMinutes < min) {
                    nextIdx = i
                    break
                }
                if (i == timesList.size - 1) {
                    nextIdx = 0 // wraps to tomorrow Fajr
                }
            }

            val prayerTimes = listOf(
                PrayerTime("Fajr", "فجر", "फ़ज्र", "الفجر", fajr, isNext = nextIdx == 0, isPast = currentMinutes >= parseMin(fajr)),
                PrayerTime("Sunrise", "طلوعِ آفتاب", "सूर्योदय", "الشروق", sunrise, isNext = nextIdx == 1, isPast = currentMinutes >= parseMin(sunrise)),
                PrayerTime("Dhuhr", "ظہر", "ज़ुहर", "الظهر", dhuhr, isNext = nextIdx == 2, isPast = currentMinutes >= parseMin(dhuhr)),
                PrayerTime("Asr", "عصر", "असर", "العصر", asr, isNext = nextIdx == 3, isPast = currentMinutes >= parseMin(asr)),
                PrayerTime("Maghrib", "مغرب", "मग्रिब", "المغرب", maghrib, isNext = nextIdx == 4, isPast = currentMinutes >= parseMin(maghrib)),
                PrayerTime("Isha", "عشاء", "इशा", "العشاء", isha, isNext = nextIdx == 5, isPast = currentMinutes >= parseMin(isha))
            )

            val result = AladhanResult(
                prayerTimes = prayerTimes,
                hijriDateFormatted = hijriFormatted,
                gregorianDateFormatted = gregDateFormatted,
                timezone = timezone,
                methodName = methodName,
                latitude = latitude,
                longitude = longitude
            )

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
