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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import pub.yusuke.fusedlocationktx.toFormattedString
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme

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

    var shout by remember { mutableStateOf("") }
    var selectedVenueIdState by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    InterscheckinTheme {
        Scaffold(
            modifier = modifier,
            scaffoldState = scaffoldState,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                VenueList(
                    venuesState = venuesState,
                    locationState = locationState,
                    selectedVenueIdState = selectedVenueIdState,
                    onClickVenue = { selectedVenueIdState = it },
                    onLongClickVenue = { venueId ->
                        viewModel.onVibrationRequested()
                        coroutineScope.launch {
                            viewModel.checkIn(venueId, shout)
                            shout = ""
                        }
                    },
                )
                Text(
                    text = when (val it = locationState) {
                        is MainContract.LocationState.Loading -> stringResource(R.string.main_location_loading)
                        is MainContract.LocationState.Loaded -> stringResource(
                            R.string.main_location_loaded,
                            it.location.toFormattedString(),
                            it.location.accuracy,
                        )
                        is MainContract.LocationState.Error -> it.throwable.stackTraceToString()
                    },
                )
                TextField(
                    value = shout,
                    onValueChange = { shout = it },
                    modifier = Modifier
                        .height(150.dp)
                        .semantics { contentDescription = "TextField for shout" },
                    placeholder = { Text(stringResource(R.string.main_shout_placeholder)) },
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.checkIn(selectedVenueIdState, shout)
                                shout = ""
                            }
                        },
                        enabled = locationState is MainContract.LocationState.Loaded &&
                            checkinState !is MainContract.CheckinState.Loading &&
                            selectedVenueIdState != "",
                        modifier = Modifier
                            .semantics { contentDescription = "Button for creating a Checkin" },
                    ) {
                        Text(stringResource(R.string.main_button_checkin))
                    }
                    Row(
                        modifier = Modifier
                            .toggleable(
                                value = drivingModeState,
                                role = Role.Checkbox,
                                onValueChange = {
                                    coroutineScope.launch {
                                        viewModel.onDrivingModeStateChanged(
                                            !drivingModeState,
                                        )
                                    }
                                },
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = drivingModeState,
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
                    onClick = { coroutineScope.launch { viewModel.onLocationUpdateRequested() } },
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
                        onClick = {
                            navController.navigate(
                                InterscheckinScreens.Histories.route,
                            )
                        },
                    ) {
                        Text(stringResource(R.string.main_button_histories))
                    }
                    Button(
                        onClick = {
                            navController.navigate(
                                InterscheckinScreens.Settings.route,
                            )
                        },
                    ) {
                        Text(stringResource(R.string.main_go_to_settings_button_label))
                    }
                }
            }
        }
    }

    SnackbarStateHandler(
        snackbarState = snackbarState,
        snackbarHostState = scaffoldState.snackbarHostState,
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

    LaunchedEffect(snackbarState) {
        snackbarMessage?.let { message ->
            when (snackbarHostState.showSnackbar(message, snackbarActionLabel)) {
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
    is MainContract.SnackbarState.UnexpectedError -> null
}

@Composable
private fun MainContract.SnackbarState.message() = when (this) {
    MainContract.SnackbarState.None -> null
    MainContract.SnackbarState.CredentialsNotSet -> stringResource(R.string.settings_reason_credentials_not_set)
    MainContract.SnackbarState.InvalidCredentials -> stringResource(R.string.settings_reason_invalid_credentials)
    MainContract.SnackbarState.SkipUpdatingVenueList -> stringResource(R.string.main_venues_not_updated_as_location_is_not_changed)
    is MainContract.SnackbarState.UnexpectedError -> stringResource(R.string.main_unexpected_error_happened, this.throwable)
}

private fun MainContract.SnackbarState.navigation() = when (this) {
    MainContract.SnackbarState.None -> null
    MainContract.SnackbarState.CredentialsNotSet -> InterscheckinScreens.Settings.route
    MainContract.SnackbarState.InvalidCredentials -> InterscheckinScreens.Settings.route
    MainContract.SnackbarState.SkipUpdatingVenueList -> null
    is MainContract.SnackbarState.UnexpectedError -> null
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
    selectedVenueIdState: String,
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
                    selectedVenueIdState = selectedVenueIdState,
                    onClickVenue = onClickVenue,
                    onLongClickVenue = onLongClickVenue,
                )
            is MainContract.VenuesState.Loading ->
                VenueColumn(
                    venues = venuesState.lastVenues,
                    selectedVenueIdState = selectedVenueIdState,
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
    Box {
        Box(
            modifier = Modifier
                .matchParentSize()
                .applyIf(venue.name.contains("交差点")) {
                    background(Color.Green.copy(alpha = 0.3f))
                },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .combinedClickable(
                    onClick = { onClick.invoke(venue.id) },
                    onLongClick = { onLongClick.invoke(venue.id) },
                ),
        ) {
            if (venue.icon == null) {
                Text("no icon")
            } else {
                Image(
                    painter = rememberAsyncImagePainter(venue.icon.url),
                    contentDescription = venue.icon.name,
                    modifier = Modifier.size(32.dp),
                )
            }
            Column {
                Row {
                    Text("${venue.name} (${venue.distance ?: "?"} m)")
                }
                Text(venue.categoriesString)
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

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun MainActivityScreenPreview() {
    MainScreen(
        viewModel = object : MainContract.ViewModel {
            override var venuesState: State<MainContract.VenuesState> =
                remember { mutableStateOf(MainContract.VenuesState.Idle(listOf(previewVenue).toImmutableList())) }
            override val snackbarMessageState: State<MainContract.SnackbarState> = remember { mutableStateOf(MainContract.SnackbarState.None) }

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
        },
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
