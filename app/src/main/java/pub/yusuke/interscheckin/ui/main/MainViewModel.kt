package pub.yusuke.interscheckin.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.os.VibrationEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.foursquareclient.FoursquareClientImpl
import pub.yusuke.interscheckin.R
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MainContract.Interactor
) : ViewModel(), MainContract.ViewModel {
    /**
     * Venue の更新が必要であるか
     * 例：Location が更新された、driving mode が更新された等
     */
    private var requireVenueUpdate = false

    override var drivingModeFlow: Flow<Boolean> = interactor.fetchDrivingModeFlow()

    private var _checkinState: MutableState<MainContract.CheckinState> =
        mutableStateOf(MainContract.CheckinState.InitialIdle)
    override val checkinState: State<MainContract.CheckinState> = _checkinState

    private var _venuesState: MutableState<MainContract.VenuesState> =
        mutableStateOf(MainContract.VenuesState.Idle(emptyList()))
    override var venuesState: State<MainContract.VenuesState> = _venuesState

    private var _locationState: MutableState<MainContract.LocationState> =
        mutableStateOf(MainContract.LocationState.Loading())
    override val locationState: State<MainContract.LocationState> = _locationState

    private var _navigationRequiredState: MutableState<String?> = mutableStateOf(null)
    override val navigationRequiredState: State<String?> = _navigationRequiredState

    private var _snackbarMessageState: MutableState<Int?> = mutableStateOf(null)
    override val snackbarMessageState: State<Int?> = _snackbarMessageState

    /**
     * `venuesState` の値に応じて `venuesState` を更新します。
     */
    private suspend fun updateVenuesState() {
        // 最後に保持していた Venue を Loading 中の状態に引き継ぐ
        val lastVenues = when (val it = venuesState.value) {
            is MainContract.VenuesState.Idle ->
                it.venues
            is MainContract.VenuesState.Loading ->
                it.lastVenues
            is MainContract.VenuesState.Error ->
                emptyList()
        }
        _venuesState.value = MainContract.VenuesState.Loading(
            lastVenues = lastVenues
        )

        _venuesState.value = when (val it = locationState.value) {
            is MainContract.LocationState.Loading ->
                MainContract.VenuesState.Idle(emptyList())
            is MainContract.LocationState.Error ->
                MainContract.VenuesState.Error(it.throwable)
            is MainContract.LocationState.Loaded ->
                MainContract.VenuesState.Idle(
                    try {
                        interactor.fetchVenues(
                            latitude = it.location.latitude,
                            longitude = it.location.longitude,
                            hacc = it.location.accuracy.toDouble(),
                            limit = 50,
                            query = if (drivingModeFlow.firstOrNull() == true) "交差" else ""
                        ).sortedBy { it.distance }
                    } catch (e: FoursquareClient.InvalidRequestTokenException) {
                        // 認証情報を更新してもらうために画面を遷移させる
                        _navigationRequiredState.value =
                            "settings/${R.string.settings_reason_invalid_credentials}"
                        emptyList()
                    } catch (e: FoursquareClientImpl.EmptyAPIKeyException) {
                        // 認証情報を設定してもらうために画面を遷移させる（おそらく初回起動時）
                        _navigationRequiredState.value =
                            "settings/${R.string.settings_reason_credentials_not_set}"
                        emptyList()
                    }
                )
        }

        // Venue の更新が完了したので、以降は他の要因がなければ更新の必要がない
        requireVenueUpdate = false
    }

    /**
     * 与えられた venueId に対応する箇所でチェックインを行います。
     */
    override suspend fun checkIn(
        venueId: String,
        shout: String?
    ) {
        val location: Location = when (val it = _locationState.value) {
            is MainContract.LocationState.Loading -> requireNotNull(it.lastLocation)
            is MainContract.LocationState.Loaded -> it.location
            else -> throw IllegalStateException("checkIn called while location is unavailable")
        }

        _checkinState.value = MainContract.CheckinState.Loading

        _checkinState.value = try {
            val checkIn = createCheckin(
                venueId = venueId,
                shout = shout ?: "",
                latitude = location.latitude,
                longitude = location.longitude
            )

            MainContract.CheckinState.Idle(checkIn)
        } catch (e: Exception) {
            MainContract.CheckinState.Error(e)
        }
    }

    override suspend fun onDrivingModeStateChanged(enabled: Boolean) {
        interactor.enableDrivingMode(enabled)
        requireVenueUpdate = true
    }

    private suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double
    ): MainContract.Checkin =
        interactor.createCheckin(
            venueId = venueId,
            shout = shout,
            latitude = latitude,
            longitude = longitude
        )

    override fun onVibrationRequested() =
        interactor.vibrate(
            VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
        )

    @SuppressLint("MissingPermission")
    override suspend fun onLocationUpdateRequested() {
        val lastLocation = when (val it = locationState.value) {
            is MainContract.LocationState.Loading ->
                it.lastLocation
            is MainContract.LocationState.Loaded ->
                it.location
            is MainContract.LocationState.Error ->
                null
        }
        _locationState.value = MainContract.LocationState.Loading(lastLocation)

        val location = interactor.fetchLocation()
        _locationState.value = MainContract.LocationState.Loaded(
            location = location
        )

        // 最後の Location と異なっている場合は Venue の update をする必要がある
        requireVenueUpdate = requireVenueUpdate || (lastLocation != location)

        if (requireVenueUpdate) {
            updateVenuesState()
        } else {
            _snackbarMessageState.value =
                R.string.main_venues_not_updated_as_location_is_not_changed
        }
    }

    override fun onNavigationFinished() {
        _navigationRequiredState.value = null
    }

    override fun onSnackbarDisplayed() {
        _snackbarMessageState.value = null
    }
}
