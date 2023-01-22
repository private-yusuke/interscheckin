package pub.yusuke.fusedlocationktx

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val LOG_TAG = "LocationUtils"

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
