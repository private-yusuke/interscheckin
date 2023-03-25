package pub.yusuke.interscheckin.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.os.VibrationEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.foursquareclient.FoursquareClientImpl
import pub.yusuke.interscheckin.ui.utils.emptyImmutableList
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MainContract.Interactor,
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
        mutableStateOf(MainContract.VenuesState.Idle(emptyImmutableList()))
    override var venuesState: State<MainContract.VenuesState> = _venuesState

    private var _locationState: MutableState<MainContract.LocationState> =
        mutableStateOf(MainContract.LocationState.Loading())
    override val locationState: State<MainContract.LocationState> = _locationState

    private var _snackbarMessageState: MutableState<MainContract.SnackbarState> =
        mutableStateOf(MainContract.SnackbarState.None)
    override val snackbarMessageState: State<MainContract.SnackbarState> = _snackbarMessageState

    init {
        if (checkLocationAccessAvailable()) {
            viewModelScope.launch {
                onLocationUpdateRequested()
            }
        }
    }

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
            lastVenues = lastVenues.toImmutableList(),
        )

        when (val it = locationState.value) {
            is MainContract.LocationState.Loading ->
                MainContract.VenuesState.Idle(emptyImmutableList())

            is MainContract.LocationState.Error ->
                MainContract.VenuesState.Error(it.throwable)

            is MainContract.LocationState.Loaded ->
                runCatching {
                    fetchSortedVenues(it.location).toImmutableList()
                }.onSuccess {
                    _venuesState.value = MainContract.VenuesState.Idle(it)
                }.onFailure {
                    _snackbarMessageState.value =
                        when (it) {
                            is FoursquareClient.InvalidRequestTokenException ->
                                MainContract.SnackbarState.InvalidCredentials

                            is FoursquareClientImpl.EmptyAPIKeyException ->
                                MainContract.SnackbarState.CredentialsNotSet

                            else ->
                                MainContract.SnackbarState.UnexpectedError(it)
                        }
                    _venuesState.value = MainContract.VenuesState.Idle(emptyImmutableList())
                }

            MainContract.LocationState.Unavailable -> error("Venues cannot be loaded while locationState is Unavailable")
        }

        // Venue の更新が完了したので、以降は他の要因がなければ更新の必要がない
        requireVenueUpdate = false
    }

    /**
     * 与えられた venueId に対応する箇所でチェックインを行います。
     */
    override suspend fun checkIn(
        venueId: String,
        shout: String?,
    ) {
        val location: Location = when (val it = _locationState.value) {
            is MainContract.LocationState.Loading -> requireNotNull(it.lastLocation)
            is MainContract.LocationState.Loaded -> it.location
            is MainContract.LocationState.Error,
            MainContract.LocationState.Unavailable,
            ->
                error("checkIn called while location is unavailable")
        }

        _checkinState.value = MainContract.CheckinState.Loading

        _checkinState.value = runCatching {
            createCheckin(
                venueId = venueId,
                shout = shout ?: "",
                latitude = location.latitude,
                longitude = location.longitude,
            )
        }.fold(
            onSuccess = {
                MainContract.CheckinState.Idle(it)
            },
            onFailure = {
                MainContract.CheckinState.Error(it)
            },
        )
    }

    override suspend fun onDrivingModeStateChanged(enabled: Boolean) {
        interactor.enableDrivingMode(enabled)
        requireVenueUpdate = true
    }

    private suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double,
    ): MainContract.Checkin =
        interactor.createCheckin(
            venueId = venueId,
            shout = shout,
            latitude = latitude,
            longitude = longitude,
        )

    override fun onVibrationRequested() =
        interactor.vibrate(
            VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE),
        )

    @SuppressLint("MissingPermission")
    override suspend fun onLocationUpdateRequested() {
        val lastLocation = when (val it = locationState.value) {
            is MainContract.LocationState.Loading ->
                it.lastLocation

            is MainContract.LocationState.Loaded ->
                it.location

            is MainContract.LocationState.Error,
            MainContract.LocationState.Unavailable,
            ->
                null
        }
        _locationState.value = MainContract.LocationState.Loading(lastLocation)

        val location =
            withContext(viewModelScope.coroutineContext + Dispatchers.IO) { interactor.fetchLocation() }
        _locationState.value = MainContract.LocationState.Loaded(
            location = location,
        )

        // 最後の Location と異なっている場合は Venue の update をする必要がある
        requireVenueUpdate = requireVenueUpdate || (lastLocation != location)

        if (requireVenueUpdate) {
            updateVenuesState()
        } else {
            _snackbarMessageState.value = MainContract.SnackbarState.SkipUpdatingVenueList
        }
    }

    override fun onSnackbarDismissed() {
        _snackbarMessageState.value = MainContract.SnackbarState.None
    }

    private suspend fun fetchSortedVenues(location: Location) =
        interactor.fetchVenues(
            latitude = location.latitude,
            longitude = location.longitude,
            hacc = location.accuracy.toDouble(),
            limit = 50,
            query = if (drivingModeFlow.firstOrNull() == true) "交差" else "",
        ).sortedBy { it.distance }

    /**
     * 位置情報へのアクセスが可能であるならば true を、そうでなければ false を返します。
     * 副作用として、位置情報へのアクセスの状況に基づいて SnackbarState や {Location,Venues}State を変更します。
     */
    private fun checkLocationAccessAvailable(): Boolean {
        if (!interactor.locationProvidersAvailable()) {
            _snackbarMessageState.value = MainContract.SnackbarState.LocationProvidersNotAvailable
            _locationState.value = MainContract.LocationState.Unavailable
            _venuesState.value = MainContract.VenuesState.Idle(emptyImmutableList())
            return false
        } else if (!interactor.preciseLocationAccessAvailable()) {
            _snackbarMessageState.value = MainContract.SnackbarState.PreciseLocationNotAvailable
        }
        return true
    }
}
