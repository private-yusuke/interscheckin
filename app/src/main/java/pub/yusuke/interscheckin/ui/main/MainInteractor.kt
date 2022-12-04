package pub.yusuke.interscheckin.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.VibrationEffect
import android.os.VibratorManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.Venue
import pub.yusuke.foursquareclient.models.url
import pub.yusuke.fusedlocationktx.locationFlow
import pub.yusuke.interscheckin.repositories.UserPreferencesRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquareCheckinsRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquarePlacesRepository
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val foursquarePlacesRepository: FoursquarePlacesRepository,
    private val foursquareCheckinsRepository: FoursquareCheckinsRepository,
    @ApplicationContext private val context: Context
) : MainContract.Interactor {
    private val vibratorManager =
        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun fetchVenues(
        latitude: Double,
        longitude: Double,
        hacc: Double?,
        limit: Int?,
        query: String?
    ): List<MainContract.Venue> =
        foursquarePlacesRepository.searchPlacesNearby(
            latitude = latitude,
            longitude = longitude,
            hacc = hacc,
            limit = limit,
            query = query
        ).translateToMainContractVenues()

    @SuppressLint("MissingPermission")
    override suspend fun fetchLocation(): Location {
        val locationRequest = LocationRequest.Builder(16)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(16)
            .build()

        return fusedLocationProviderClient
            .locationFlow(locationRequest)
            .first()
    }

    override fun fetchDrivingModeFlow(): Flow<Boolean> =
        userPreferencesRepository
            .userPreferencesFlow
            .map { it.drivingMode }

    override suspend fun enableDrivingMode(enabled: Boolean) =
        userPreferencesRepository.enableDrivingMode(enabled)

    override suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double
    ): MainContract.Checkin = foursquareCheckinsRepository.createCheckin(
        venueId = venueId,
        shout = shout,
        latitude = latitude,
        longitude = longitude
    ).translateToMainContractCheckin()

    override fun vibrate(vibrationEffect: VibrationEffect) =
        vibratorManager.defaultVibrator.vibrate(vibrationEffect)

    private fun List<Venue>.translateToMainContractVenues(): List<MainContract.Venue> =
        map { it.translateToMainContractVenue() }

    private fun Venue.translateToMainContractVenue(): MainContract.Venue =
        MainContract.Venue(
            id = this.fsq_id,
            name = this.name,
            categoriesString = this.categories.joinToString(", ") { it.name },
            distance = this.distance,
            icon = this.categories.firstOrNull()?.let { category ->
                MainContract.Venue.Icon(
                    name = category.name,
                    url = category.icon.url()
                )
            }
        )

    private fun Checkin.translateToMainContractCheckin(): MainContract.Checkin =
        MainContract.Checkin(
            venueName = this.venue.name
        )
}
