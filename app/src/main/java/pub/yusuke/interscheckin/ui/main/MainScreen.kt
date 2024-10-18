package pub.yusuke.interscheckin.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import coil.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import pub.yusuke.fusedlocationktx.toFormattedString
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
import pub.yusuke.interscheckin.ui.theme.InterscheckinTextStyle
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme

private sealed interface MainScreenType {
    data object Compact : MainScreenType
    data object PortraitMedium : MainScreenType
    data object Extra : MainScreenType
}

@Composable
private fun screenType(windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass) = when {
    windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT &&
        windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT ->
        MainScreenType.Compact
    windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT &&
        windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT ->
        MainScreenType.PortraitMedium
    else -> MainScreenType.Extra
}

@Composable
private fun MainScreenContent(
    locationState: MainContract.LocationState,
    shout: String,
    onShoutChange: (String) -> Unit,
    onCheckinButtonClicked: () -> Unit,
    checkinState: MainContract.CheckinState,
    venuesState: MainContract.VenuesState,
    onClickVenue: (String) -> Unit,
    onLongClickVenue: (String) -> Unit,
    selectedVenueId: String,
    onDrivingModeCheckboxClicked: (Boolean) -> Unit,
    drivingEnabled: Boolean,
    onUpdateVenueListButtonClicked: () -> Unit,
    periodicLocationRetrievalEnabledState: MainContract.PeriodicLocationRetrievalState,
    onHistoriesButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onPeriodicLocationRetrievalSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    val screen = screenType(windowSizeClass)

    when (screen) {
        MainScreenType.Compact -> MainScreenCompactContent(
            locationState,
            shout,
            onShoutChange,
            onCheckinButtonClicked,
            checkinState,
            venuesState,
            onClickVenue,
            onLongClickVenue,
            selectedVenueId,
            onDrivingModeCheckboxClicked,
            drivingEnabled,
            onUpdateVenueListButtonClicked,
            periodicLocationRetrievalEnabledState,
            onHistoriesButtonClicked,
            onSettingsButtonClicked,
            onPeriodicLocationRetrievalSettingsButtonClicked, modifier,
        )
        MainScreenType.PortraitMedium -> MainScreenPortraitMediumContent(
            locationState,
            shout,
            onShoutChange,
            onCheckinButtonClicked,
            checkinState,
            venuesState,
            onClickVenue,
            onLongClickVenue,
            selectedVenueId,
            onDrivingModeCheckboxClicked,
            drivingEnabled,
            onUpdateVenueListButtonClicked,
            periodicLocationRetrievalEnabledState,
            onHistoriesButtonClicked,
            onSettingsButtonClicked,
            onPeriodicLocationRetrievalSettingsButtonClicked, modifier,
        )
        MainScreenType.Extra -> MainScreenExtraContent(
            locationState,
            shout,
            onShoutChange,
            onCheckinButtonClicked,
            checkinState,
            venuesState,
            onClickVenue,
            onLongClickVenue,
            selectedVenueId,
            onDrivingModeCheckboxClicked,
            drivingEnabled,
            onUpdateVenueListButtonClicked,
            periodicLocationRetrievalEnabledState,
            onHistoriesButtonClicked,
            onSettingsButtonClicked,
            onPeriodicLocationRetrievalSettingsButtonClicked, modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenCompactContent(
    locationState: MainContract.LocationState,
    shout: String,
    onShoutChange: (String) -> Unit,
    onCheckinButtonClicked: () -> Unit,
    checkinState: MainContract.CheckinState,
    venuesState: MainContract.VenuesState,
    onClickVenue: (String) -> Unit,
    onLongClickVenue: (String) -> Unit,
    selectedVenueId: String,
    onDrivingModeCheckboxClicked: (Boolean) -> Unit,
    drivingEnabled: Boolean,
    onUpdateVenueListButtonClicked: () -> Unit,
    periodicLocationRetrievalEnabledState: MainContract.PeriodicLocationRetrievalState,
    onHistoriesButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onPeriodicLocationRetrievalSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 40.dp,
        sheetContent = {
            ControlsColumn(
                locationState,
                shout,
                onShoutChange,
                onCheckinButtonClicked,
                checkinState,
                selectedVenueId,
                onDrivingModeCheckboxClicked,
                drivingEnabled,
                onUpdateVenueListButtonClicked,
                periodicLocationRetrievalEnabledState,
                onHistoriesButtonClicked,
                onSettingsButtonClicked,
                onPeriodicLocationRetrievalSettingsButtonClicked,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
            VenueList(
                venuesState,
                locationState,
                selectedVenueId,
                onClickVenue,
                onLongClickVenue,
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun MainScreenExtraContent(
    locationState: MainContract.LocationState,
    shout: String,
    onShoutChange: (String) -> Unit,
    onCheckinButtonClicked: () -> Unit,
    checkinState: MainContract.CheckinState,
    venuesState: MainContract.VenuesState,
    onClickVenue: (String) -> Unit,
    onLongClickVenue: (String) -> Unit,
    selectedVenueId: String,
    onDrivingModeCheckboxClicked: (Boolean) -> Unit,
    drivingEnabled: Boolean,
    onUpdateVenueListButtonClicked: () -> Unit,
    periodicLocationRetrievalEnabledState: MainContract.PeriodicLocationRetrievalState,
    onHistoriesButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onPeriodicLocationRetrievalSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier,
    ) {
        VenueList(
            venuesState,
            locationState,
            selectedVenueId,
            onClickVenue,
            onLongClickVenue,
            modifier = Modifier
                .weight(0.4f),
        )
        ControlsColumn(
            locationState,
            shout,
            onShoutChange,
            onCheckinButtonClicked,
            checkinState,
            selectedVenueId,
            onDrivingModeCheckboxClicked,
            drivingEnabled,
            onUpdateVenueListButtonClicked,
            periodicLocationRetrievalEnabledState,
            onHistoriesButtonClicked,
            onSettingsButtonClicked,
            onPeriodicLocationRetrievalSettingsButtonClicked,
            modifier = Modifier
                .weight(0.6f)
                .padding(vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Composable
private fun MainScreenPortraitMediumContent(
    locationState: MainContract.LocationState,
    shout: String,
    onShoutChange: (String) -> Unit,
    onCheckinButtonClicked: () -> Unit,
    checkinState: MainContract.CheckinState,
    venuesState: MainContract.VenuesState,
    onClickVenue: (String) -> Unit,
    onLongClickVenue: (String) -> Unit,
    selectedVenueId: String,
    onDrivingModeCheckboxClicked: (Boolean) -> Unit,
    drivingEnabled: Boolean,
    onUpdateVenueListButtonClicked: () -> Unit,
    periodicLocationRetrievalEnabledState: MainContract.PeriodicLocationRetrievalState,
    onHistoriesButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onPeriodicLocationRetrievalSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        VenueList(
            venuesState,
            locationState,
            selectedVenueId,
            onClickVenue,
            onLongClickVenue,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.4f),
        )
        ControlsColumn(
            locationState,
            shout,
            onShoutChange,
            onCheckinButtonClicked,
            checkinState,
            selectedVenueId,
            onDrivingModeCheckboxClicked,
            drivingEnabled,
            onUpdateVenueListButtonClicked,
            periodicLocationRetrievalEnabledState,
            onHistoriesButtonClicked,
            onSettingsButtonClicked,
            onPeriodicLocationRetrievalSettingsButtonClicked,
            modifier = Modifier
                .weight(0.6f)
                .padding(vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
        )
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MainContract.ViewModel = hiltViewModel<MainViewModel>(),
) {
    val drivingModeState by viewModel.drivingModeFlow
        .collectAsState(initial = false)
    val venuesState by viewModel.venuesState
    val locationState by viewModel.locationState
    val checkinState by viewModel.checkinState
    val snackbarState by viewModel.snackbarMessageState
    val periodicLocationRetrievalEnabledState by viewModel.periodicLocationRetrievalEnabledState

    var shout by rememberSaveable { mutableStateOf("") }
    var selectedVenueIdState by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    InterscheckinTheme {
        Scaffold(
            modifier = modifier,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            MainScreenContent(
                locationState,
                shout,
                onShoutChange = { shout = it },
                onCheckinButtonClicked = {
                    coroutineScope.launch {
                        viewModel.checkIn(selectedVenueIdState, shout)
                        shout = ""
                    }
                },
                checkinState,
                venuesState,
                onClickVenue = { selectedVenueIdState = it },
                onLongClickVenue = { venueId ->
                    viewModel.onVibrationRequested()
                    coroutineScope.launch {
                        viewModel.checkIn(venueId, shout)
                        shout = ""
                    }
                },
                selectedVenueId = selectedVenueIdState,
                onDrivingModeCheckboxClicked = {
                    coroutineScope.launch {
                        viewModel.onDrivingModeStateChanged(
                            !drivingModeState,
                        )
                    }
                },
                drivingEnabled = drivingModeState,
                onUpdateVenueListButtonClicked = { coroutineScope.launch { viewModel.onLocationUpdateRequested() } },
                periodicLocationRetrievalEnabledState = periodicLocationRetrievalEnabledState,
                onHistoriesButtonClicked = {
                    navController.navigate(
                        InterscheckinScreens.Histories.route,
                    )
                },
                onSettingsButtonClicked = {
                    navController.navigate(
                        InterscheckinScreens.Settings.route,
                    )
                },
                onPeriodicLocationRetrievalSettingsButtonClicked = {
                    navController.navigate(InterscheckinScreens.LocationSettings.route)
                },
                modifier = Modifier
                    .padding(innerPadding),
            )
        }
    }

    SnackbarStateHandler(
        snackbarState = snackbarState,
        snackbarHostState = snackbarHostState,
        navController = navController,
        onSnackbarDismissed = viewModel::onSnackbarDismissed,
    )
}

@Composable
private fun SnackbarStateHandler(
    snackbarState: MainContract.SnackbarState,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    onSnackbarDismissed: () -> Unit,
) {
    val snackbarMessage = snackbarState.message()
    val snackbarActionLabel = snackbarState.actionLabel()
    val snackbarDuration = snackbarState.duration()

    LaunchedEffect(snackbarState) {
        snackbarMessage?.let { message ->
            when (
                snackbarHostState.showSnackbar(
                    message,
                    snackbarActionLabel,
                    true,
                    snackbarDuration,
                )
            ) {
                SnackbarResult.Dismissed -> onSnackbarDismissed.invoke()
                SnackbarResult.ActionPerformed -> {
                    snackbarState.navigation()?.let { navigation ->
                        navController.navigate(navigation)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainContract.SnackbarState.actionLabel() = when (this) {
    MainContract.SnackbarState.None -> null
    MainContract.SnackbarState.CredentialsNotSet -> stringResource(R.string.main_settings)
    MainContract.SnackbarState.InvalidCredentials -> stringResource(R.string.main_settings)
    MainContract.SnackbarState.SkipUpdatingVenueList -> null
    MainContract.SnackbarState.LocationProvidersNotAvailable -> stringResource(R.string.main_snackbar_location_providers_not_available_action)
    MainContract.SnackbarState.PreciseLocationNotAvailable -> stringResource(R.string.main_snackbar_location_providers_not_available_action)
    is MainContract.SnackbarState.UnexpectedError -> null
}

@Composable
private fun MainContract.SnackbarState.message() = when (this) {
    MainContract.SnackbarState.None -> null
    MainContract.SnackbarState.CredentialsNotSet -> stringResource(R.string.main_snackbar_credentials_not_set)
    MainContract.SnackbarState.InvalidCredentials -> stringResource(R.string.main_snackbar_invalid_credentials)
    MainContract.SnackbarState.SkipUpdatingVenueList -> stringResource(R.string.main_venues_not_updated_as_location_is_not_changed)
    MainContract.SnackbarState.LocationProvidersNotAvailable -> stringResource(R.string.main_snackbar_location_providers_not_available_message)
    MainContract.SnackbarState.PreciseLocationNotAvailable -> stringResource(R.string.main_snackbar_precise_location_not_available_message)
    is MainContract.SnackbarState.UnexpectedError -> stringResource(R.string.main_unexpected_error_happened, this.throwable)
}

private fun MainContract.SnackbarState.duration() = when (this) {
    MainContract.SnackbarState.CredentialsNotSet,
    MainContract.SnackbarState.InvalidCredentials,
    MainContract.SnackbarState.LocationProvidersNotAvailable,
    MainContract.SnackbarState.PreciseLocationNotAvailable,
    ->
        SnackbarDuration.Indefinite
    MainContract.SnackbarState.None,
    MainContract.SnackbarState.SkipUpdatingVenueList,
    is MainContract.SnackbarState.UnexpectedError,
    ->
        SnackbarDuration.Short
}

private fun MainContract.SnackbarState.navigation() = when (this) {
    MainContract.SnackbarState.None,
    MainContract.SnackbarState.SkipUpdatingVenueList,
    is MainContract.SnackbarState.UnexpectedError,
    ->
        null
    MainContract.SnackbarState.CredentialsNotSet,
    MainContract.SnackbarState.InvalidCredentials,
    ->
        InterscheckinScreens.CredentialSettings.route
    MainContract.SnackbarState.LocationProvidersNotAvailable,
    MainContract.SnackbarState.PreciseLocationNotAvailable,
    ->
        InterscheckinScreens.LocationAccessAcquirement.route
}

@Composable
private fun VenueColumn(
    venues: ImmutableList<MainContract.Venue>,
    selectedVenueIdState: String,
    onClickVenue: (String) -> Unit,
    onLongClickVenue: (String) -> Unit,
) {
    LazyColumn {
        items(venues) { venue ->
            VenueRow(
                selected = venue.id == selectedVenueIdState,
                onClick = onClickVenue,
                onLongClick = onLongClickVenue,
                venue = venue,
            )
        }
    }
}

@Composable
fun VenueList(
    venuesState: MainContract.VenuesState,
    locationState: MainContract.LocationState,
    selectedVenueId: String,
    onClickVenue: (String) -> Unit,
    onLongClickVenue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        when (venuesState) {
            is MainContract.VenuesState.Error ->
                Text(
                    modifier = Modifier
                        .height(225.dp)
                        .fillMaxWidth(),
                    text = "Error: ${venuesState.throwable.stackTraceToString()}",
                )
            is MainContract.VenuesState.Idle ->
                VenueColumn(
                    venues = venuesState.venues,
                    selectedVenueIdState = selectedVenueId,
                    onClickVenue = onClickVenue,
                    onLongClickVenue = onLongClickVenue,
                )
            is MainContract.VenuesState.Loading ->
                VenueColumn(
                    venues = venuesState.lastVenues,
                    selectedVenueIdState = selectedVenueId,
                    onClickVenue = onClickVenue,
                    onLongClickVenue = onLongClickVenue,
                )
        }
        if (
            locationState is MainContract.LocationState.Loading ||
            venuesState is MainContract.VenuesState.Loading
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VenueRow(
    selected: Boolean,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    venue: MainContract.Venue,
) {
    val intersectionItemBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
    Box(
        modifier = Modifier
            .applyIf(venue.name.contains("交差点")) {
                background(intersectionItemBackgroundColor)
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .combinedClickable(
                    onClick = { onClick.invoke(venue.id) },
                    onLongClick = { onLongClick.invoke(venue.id) },
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (venue.icon == null) {
                Text("no icon")
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(percent = 100))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(venue.icon.url),
                        contentDescription = venue.icon.name,
                        modifier = Modifier.size(40.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface),
                    )
                }
            }
            Column {
                Row {
                    Text(
                        fontSize = 5.em,
                        text = "${venue.name} (${venue.distance ?: "?"} m)",
                    )
                }
                Text(
                    fontSize = 4.em,
                    text = venue.categoriesString,
                )
                if (selected) {
                    Text(
                        modifier = Modifier
                            .background(Color.Red),
                        text = stringResource(R.string.main_venue_selected),
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlsColumn(
    locationState: MainContract.LocationState,
    shout: String,
    onShoutChange: (String) -> Unit,
    onCheckinButtonClicked: () -> Unit,
    checkinState: MainContract.CheckinState,
    selectedVenueId: String,
    onDrivingModeCheckboxClicked: (Boolean) -> Unit,
    drivingEnabled: Boolean,
    onUpdateVenueListButtonClicked: () -> Unit,
    periodicLocationRetrievalEnabledState: MainContract.PeriodicLocationRetrievalState,
    onHistoriesButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onPeriodicLocationRetrievalSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = when (val it = locationState) {
                is MainContract.LocationState.Loading -> stringResource(R.string.main_location_loading)
                is MainContract.LocationState.Loaded -> stringResource(
                    R.string.main_location_loaded,
                    it.location.toFormattedString(),
                    it.location.accuracy,
                )

                is MainContract.LocationState.Error -> it.throwable.stackTraceToString()
                MainContract.LocationState.Unavailable -> stringResource(R.string.main_location_unavailable)
            },
        )
        TextField(
            value = shout,
            onValueChange = onShoutChange,
            modifier = Modifier
                .height(150.dp)
                .semantics { contentDescription = "TextField for shout" },
            placeholder = { Text(stringResource(R.string.main_shout_placeholder)) },
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = onCheckinButtonClicked,
                enabled = locationState is MainContract.LocationState.Loaded &&
                    checkinState !is MainContract.CheckinState.Loading &&
                    selectedVenueId != "",
                modifier = Modifier
                    .semantics { contentDescription = "Button for creating a Checkin" },
            ) {
                Text(stringResource(R.string.main_button_checkin))
            }
            Row(
                modifier = Modifier
                    .toggleable(
                        value = drivingEnabled,
                        role = Role.Checkbox,
                        onValueChange = onDrivingModeCheckboxClicked,
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = drivingEnabled,
                    onCheckedChange = null,
                )
                Text(
                    text = stringResource(id = R.string.main_driving_mode_checkbox_label),
                )
            }
        }
        Text(
            text = when (val it = checkinState) {
                MainContract.CheckinState.InitialIdle ->
                    stringResource(R.string.main_message_lets_checkin_today)

                MainContract.CheckinState.Loading ->
                    stringResource(R.string.main_message_creating_checkin)

                is MainContract.CheckinState.Idle ->
                    stringResource(
                        R.string.main_message_checkin_success,
                        it.lastCheckin.venueName,
                    )

                is MainContract.CheckinState.Error ->
                    "Error: ${it.throwable.message}\n${it.throwable.stackTrace}"
            },
        )
        Button(
            onClick = onUpdateVenueListButtonClicked,
            enabled = locationState is MainContract.LocationState.Loaded,
        ) {
            Text(
                text = stringResource(id = R.string.main_request_venue_list_update),
                style = TextStyle.Default.copy(
                    fontSize = 40.sp,
                ),
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Button(
                onClick = onHistoriesButtonClicked,
            ) {
                Text(stringResource(R.string.main_button_histories))
            }
            Button(
                onClick = onSettingsButtonClicked,
            ) {
                Text(stringResource(R.string.main_go_to_settings_button_label))
            }
        }
        (periodicLocationRetrievalEnabledState as? MainContract.PeriodicLocationRetrievalState.Enabled)?.let {
            TextButton(
                onClick = onPeriodicLocationRetrievalSettingsButtonClicked,
            ) {
                Text(
                    text = stringResource(
                        R.string.main_periodic_location_retrieval_enabled,
                        it.interval,
                    ),
                    style = InterscheckinTextStyle.Large,
                )
            }
        }
    }
}

private val previewVenue =
    MainContract.Venue(
        id = "id",
        name = "test venue",
        categoriesString = "test category, test category 2",
        distance = 12,
        icon = MainContract.Venue.Icon(
            name = "test category",
            url = "https://example.com/foo.png",
        ),
    )

private val previewViewModel @Composable get() = object : MainContract.ViewModel {
    override var venuesState: State<MainContract.VenuesState> =
        remember { mutableStateOf(MainContract.VenuesState.Idle(listOf(previewVenue).toImmutableList())) }
    override val snackbarMessageState: State<MainContract.SnackbarState> =
        remember { mutableStateOf(MainContract.SnackbarState.None) }
    override val periodicLocationRetrievalEnabledState: State<MainContract.PeriodicLocationRetrievalState> =
        remember { mutableStateOf(MainContract.PeriodicLocationRetrievalState.Disabled) }

    override suspend fun onDrivingModeStateChanged(enabled: Boolean) {}
    override suspend fun checkIn(venueId: String, shout: String?) {}

    override fun onVibrationRequested() {}
    override suspend fun onLocationUpdateRequested() {}
    override fun onSnackbarDismissed() {}

    override val checkinState: MutableState<MainContract.CheckinState> =
        remember { mutableStateOf(MainContract.CheckinState.InitialIdle) }
    override val drivingModeFlow = flowOf(false)
    override val locationState: State<MainContract.LocationState> =
        remember { mutableStateOf(MainContract.LocationState.Loading()) }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun MainActivityScreenPreview() {
    MainScreen(
        viewModel = previewViewModel,
        navController = rememberNavController(),
    )
}

@Preview
@Composable
private fun VenueRowPreview() =
    VenueRow(
        selected = false,
        onClick = {},
        onLongClick = {},
        venue = previewVenue,
    )

@Preview
@Composable
private fun VenueRowSelectedPreview() =
    VenueRow(
        selected = true,
        onClick = {},
        onLongClick = {},
        venue = previewVenue,
    )

private fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier =
    if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, device = "spec:orientation=landscape,width=411dp,height=891dp")
@Composable
private fun MainActivityScreenPortraitPreview() {
    MainScreen(
        viewModel = previewViewModel,
        navController = rememberNavController(),
    )
}
