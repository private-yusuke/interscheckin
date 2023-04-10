package pub.yusuke.interscheckin.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.Venue
import pub.yusuke.foursquareclient.models.url
import pub.yusuke.fusedlocationktx.currentLocation
import pub.yusuke.fusedlocationktx.locationFlow
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquareCheckinsRepository
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquarePlacesRepository
import pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalPreferences
import pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalRepository
import pub.yusuke.interscheckin.repositories.userpreferences.UserPreferencesRepository
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenue
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenueDao
import javax.inject.Inject
import kotlin.math.roundToLong

class MainInteractor @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val foursquarePlacesRepository: FoursquarePlacesRepository,
    private val foursquareCheckinsRepository: FoursquareCheckinsRepository,
    private val vibratorManager: VibratorManager,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val visitedVenueDao: VisitedVenueDao,
    private val periodicLocationRetrievalRepository: PeriodicLocationRetrievalRepository,
    @ApplicationContext private val context: Context,
) : MainContract.Interactor {
    override suspend fun fetchVenues(
        latitude: Double,
        longitude: Double,
        hacc: Double?,
        limit: Int?,
        query: String?,
    ): List<MainContract.Venue> {
        val remoteVenues = foursquarePlacesRepository.searchPlacesNearby(
            latitude = latitude,
            longitude = longitude,
            hacc = hacc,
            limit = limit,
            query = query,
        ).translateToMainContractVenues()
        val localVenues = visitedVenueDao.findByLatLong(
            latitude = latitude,
            longitude = longitude,
            nameLike = query.orEmpty(),
        ).translateToMainContractVenues(latitude, longitude)

        return remoteVenues.plus(localVenues).toSet().distinctBy {
            it.id
        }.toList()
    }

    override suspend fun fetchLocation(): Location =
        fusedLocationProviderClient
            .currentLocation()

    override fun fetchLocationFlow(locationRequest: LocationRequest): Flow<Location> =
        fusedLocationProviderClient
            .locationFlow(locationRequest)

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
        longitude: Double,
    ): MainContract.Checkin = foursquareCheckinsRepository.createCheckin(
        venueId = venueId,
        shout = shout,
        latitude = latitude,
        longitude = longitude,
    ).translateToMainContractCheckin()

    override fun locationProvidersAvailable(): Boolean =
        (context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager)
            ?.let { locationManager ->
                locationManager.getProviders(true).size > 0
            } ?: false

    override fun preciseLocationAccessAvailable(): Boolean =
        (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            )

    override fun fetchPeriodicLocationRetrievalPreferencesFlow(): Flow<PeriodicLocationRetrievalPreferences> =
        periodicLocationRetrievalRepository.periodicLocationRetrievalPreferencesFlow

    override fun vibrate(vibrationEffect: VibrationEffect) =
        vibratorManager.defaultVibrator.vibrate(vibrationEffect)

    private fun List<Venue>.translateToMainContractVenues(): List<MainContract.Venue> =
        map { it.translateToMainContractVenue() }

    private fun Venue.translateToMainContractVenue(): MainContract.Venue =
        MainContract.Venue(
            id = this.fsqId,
            name = this.name,
            categoriesString = this.categories.joinToString(", ") { it.name },
            distance = this.distance,
            icon = this.categories.firstOrNull()?.let { category ->
                MainContract.Venue.Icon(
                    name = category.name,
                    url = category.icon.url(),
                )
            },
        )

    private fun List<VisitedVenue>.translateToMainContractVenues(
        latitude: Double,
        longitude: Double,
    ): List<MainContract.Venue> =
        map { it.translateToMainContractVenue(latitude, longitude) }

    private fun VisitedVenue.translateToMainContractVenue(
        latitude: Double,
        longitude: Double,
    ): MainContract.Venue =
        MainContract.Venue(
            id = this.id,
            name = this.name,
            categoriesString = this.categoriesString.orEmpty(),
            distance = calculateDistance(
                latitude,
                longitude,
                this.location.x,
                this.location.y,
            ).roundToLong(),

            // If `iconName` is not null, `iconUrl` is not null too
            icon = this.iconName?.let {
                MainContract.Venue.Icon(
                    name = it,
                    url = this.iconUrl!!,
                )
            },
        )

    private fun Checkin.translateToMainContractCheckin(): MainContract.Checkin =
        MainContract.Checkin(
            id = this.venue.id,
            venueName = this.venue.name,
            shout = this.shout,
        )

    private fun calculateDistance(
        @FloatRange(from = -90.0, to = 90.0) startLatitude: Double,
        @FloatRange(from = -180.0, to = 180.0) startLongitude: Double,
        @FloatRange(from = -90.0, to = 90.0) endLatitude: Double,
        @FloatRange(from = -180.0, to = 180.0) endLongitude: Double,
    ): Float {
        val results = FloatArray(3)
        Location.distanceBetween(
            startLatitude,
            startLongitude,
            endLatitude,
            endLongitude,
            results,
        )
        return results.first()
    }
}
