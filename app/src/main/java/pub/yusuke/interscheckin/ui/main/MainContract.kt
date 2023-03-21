package pub.yusuke.interscheckin.ui.main

import android.location.Location
import android.os.VibrationEffect
import androidx.compose.runtime.State
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface MainContract {
    interface ViewModel {
        val drivingModeFlow: Flow<Boolean>
        val locationState: State<LocationState>
        val checkinState: State<CheckinState>
        val venuesState: State<VenuesState>
        val snackbarMessageState: State<SnackbarState>

        suspend fun onDrivingModeStateChanged(enabled: Boolean)
        suspend fun checkIn(
            venueId: String,
            shout: String? = null,
        )

        fun onVibrationRequested()
        suspend fun onLocationUpdateRequested()
        fun onSnackbarDismissed()
    }

    interface Interactor {
        suspend fun fetchVenues(
            latitude: Double,
            longitude: Double,
            hacc: Double?,
            limit: Int? = null,
            query: String? = null,
        ): List<Venue>

        suspend fun fetchLocation(): Location
        fun fetchDrivingModeFlow(): Flow<Boolean>
        suspend fun enableDrivingMode(enabled: Boolean)
        suspend fun createCheckin(
            venueId: String,
            shout: String,
            latitude: Double,
            longitude: Double,
        ): Checkin

        fun vibrate(vibrationEffect: VibrationEffect)
    }

    data class Venue(
        val id: String,
        val name: String,
        val categoriesString: String,
        val distance: Long?,
        val icon: Icon?,
    ) {
        data class Icon(
            val name: String,
            val url: String,
        )
    }

    data class Checkin(
        val id: String,
        val venueName: String,
        val shout: String?,
    )

    sealed class LocationState {
        /**
         * @param lastLocation 最後に保持していた Location
         */
        class Loading(val lastLocation: Location? = null) : LocationState()
        class Loaded(val location: Location) : LocationState()
        class Error(val throwable: Throwable) : LocationState()
    }

    sealed class VenuesState {
        /**
         * @param lastVenues 最後に保持していた Venue たち
         */
        class Loading(val lastVenues: ImmutableList<Venue>) : VenuesState()
        class Idle(val venues: ImmutableList<Venue>) : VenuesState()
        class Error(val throwable: Throwable) : VenuesState()
    }

    sealed class CheckinState {
        object InitialIdle : CheckinState()
        object Loading : CheckinState()
        class Idle(val lastCheckin: Checkin) : CheckinState()
        class Error(val throwable: Throwable) : CheckinState()
    }

    sealed interface SnackbarState {
        object None : SnackbarState
        object SkipUpdatingVenueList : SnackbarState
        object InvalidCredentials : SnackbarState
        object CredentialsNotSet : SnackbarState
        data class UnexpectedError(val throwable: Throwable) : SnackbarState
    }
}
