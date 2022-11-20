package pub.yusuke.fusedlocationktx

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

private const val LOG_TAG = "LocationUtils"

fun Location.toFormattedString() = "$latitude, $longitude"

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow(
    locationRequest: LocationRequest
) = callbackFlow<Location> {
    val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                Log.d(LOG_TAG, "Sending location $location")
                trySend(location)
            }
        }
    }

    Log.d(LOG_TAG, "requestLocationUpdates")
    requestLocationUpdates(
        locationRequest,
        callback,
        Looper.getMainLooper()
    )

    awaitClose {
        Log.d(LOG_TAG, "Removing location update callback")
        removeLocationUpdates(callback)
    }
}
