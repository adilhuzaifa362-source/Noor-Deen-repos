package com.example.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun getUserCurrentLocation(
        context: Context,
        onLocationReceived: (latitude: Double, longitude: Double) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission(context)) {
            onError("Location permission not granted")
            return
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (locationManager == null) {
            onError("Location service unavailable")
            return
        }

        // Try GPS provider first, then Network provider
        val gpsLocation: Location? = try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else null
        } catch (_: Exception) { null }

        val networkLocation: Location? = try {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } else null
        } catch (_: Exception) { null }

        val bestLocation = when {
            gpsLocation != null && networkLocation != null -> {
                if (gpsLocation.time > networkLocation.time) gpsLocation else networkLocation
            }
            gpsLocation != null -> gpsLocation
            networkLocation != null -> networkLocation
            else -> null
        }

        if (bestLocation != null) {
            onLocationReceived(bestLocation.latitude, bestLocation.longitude)
            return
        }

        // If no last known location, request a single update
        val provider = when {
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
            else -> null
        }

        if (provider != null) {
            try {
                val singleUpdateListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        locationManager.removeUpdates(this)
                        onLocationReceived(location.latitude, location.longitude)
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }
                locationManager.requestSingleUpdate(provider, singleUpdateListener, null)
            } catch (e: Exception) {
                onError("Failed to request location update: ${e.message}")
            }
        } else {
            onError("GPS and Network location providers are disabled")
        }
    }
}
