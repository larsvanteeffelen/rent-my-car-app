package nl.avans.rentmycar.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object LocationUtils {

    private const val MIN_TIME_BETWEEN_UPDATES: Long = 1000 // 1 second
    private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 1.0f // 1 meter

    fun getCurrentLocation(context: Context) = callbackFlow {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    try {
                        trySend(location).isSuccess
                    } catch (e: Exception) {
                        // Channel was closed
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener
            )

            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                trySend(lastKnownLocation).isSuccess
            }

            awaitClose {
                locationManager.removeUpdates(locationListener)
            }
        } else {
            trySend(null).isSuccess // Permission not granted
            close()
        }
    }
}
