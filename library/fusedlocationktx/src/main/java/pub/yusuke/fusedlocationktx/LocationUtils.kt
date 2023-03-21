package pub.yusuke.fusedlocationktx

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
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
