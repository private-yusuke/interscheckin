package pub.yusuke.fusedlocationktx

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun Location.toFormattedString() = "$latitude, $longitude"

@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.currentLocation(): Location = suspendCoroutine { cont ->
    val currentLocationRequest = CurrentLocationRequest
        .Builder()
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    this.getCurrentLocation(currentLocationRequest, null)
        .addOnSuccessListener { location ->
            cont.resume(location)
        }
}

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.locationFlow(
    locationRequest: LocationRequest,
) = callbackFlow<Location> {
    val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
                trySend(location)
            }
        }
    }

    requestLocationUpdates(
        locationRequest,
        callback,
        Looper.getMainLooper(),
    )

    awaitClose {
        removeLocationUpdates(callback)
    }
}
