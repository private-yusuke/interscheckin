package pub.yusuke.interscheckin.ui.locationsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
import pub.yusuke.interscheckin.navigation.entity.PeriodicLocationRetrievalIntervalPreset
import pub.yusuke.interscheckin.ui.theme.InterscheckinTextStyle
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme
import pub.yusuke.interscheckin.ui.utils.InterscheckinTopBar
import pub.yusuke.interscheckin.ui.utils.copy
import pub.yusuke.interscheckin.ui.utils.locationAccessAcquired
import pub.yusuke.interscheckin.ui.utils.preciseLocationAccessAcquired
import pub.yusuke.interscheckin.ui.utils.rememberLocationAccessAcquirementState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LocationSettingsContract.ViewModel = hiltViewModel<LocationSettingsViewModel>(),
) {
    val locationPermissionState = rememberLocationAccessAcquirementState()
    val screenState by viewModel.screenStateFlow.collectAsState()

    InterscheckinTheme {
        Scaffold(
            topBar = {
                InterscheckinTopBar(
                    titleText = stringResource(R.string.location_settings_topbar_title),
                    onClickNavigationIcon = { navController.popBackStack() },
                )
            },
            modifier = modifier,
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        innerPadding.copy(
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    ),
            ) {
                when (val it = screenState) {
                    LocationSettingsContract.ScreenState.Loading -> CircularProgressIndicator()
                    is LocationSettingsContract.ScreenState.Idle -> {
                        LocationAccessAcquirementScreenColumn(
                            locationAccessAcquired = locationPermissionState.locationAccessAcquired(),
                            preciseLocationAccessAcquired = locationPermissionState.preciseLocationAccessAcquired(),
                            onClickLocationAccessAcquirementScreenButton = {
                                navController.navigate(InterscheckinScreens.LocationAccessAcquirement.route)
                            },
                        )
                        PeriodicLocationRetrievalSettingsColumn(
                            enabled = locationPermissionState.locationAccessAcquired(),
                            periodicLocationRetrievalEnabled = it.periodicLocationRetrievalEnabled,
                            onPeriodicLocationRetrievalEnabledCheckedChange = { viewModel.setPeriodicLocationRetrievalEnabled(it) },
                            periodicLocationRetrievalIntervalPreset = it.periodicLocationRetrievalIntervalPreset,
                            onPeriodicLocationRetrievalIntervalPresetChanged = { viewModel.setPeriodicLocationRetrievalPreset(it) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationAccessAcquirementScreenColumn(
    locationAccessAcquired: Boolean,
    preciseLocationAccessAcquired: Boolean,
    onClickLocationAccessAcquirementScreenButton: () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides if (!preciseLocationAccessAcquired) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.location_settings_location_access_acquirement_screen_column_title),
                style = InterscheckinTextStyle.ExtraLarge,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                LocationAccessAcquirementScreenButton(
                    locationAccessAcquired = locationAccessAcquired,
                    preciseLocationAccessAcquired = preciseLocationAccessAcquired,
                    onClick = onClickLocationAccessAcquirementScreenButton,
                )
            }
        }
    }
}

@Composable
private fun LocationAccessAcquirementScreenButton(
    locationAccessAcquired: Boolean,
    preciseLocationAccessAcquired: Boolean,
    onClick: () -> Unit,
) {
    val buttonText = when {
        preciseLocationAccessAcquired -> stringResource(R.string.credential_settings_location_access_already_acquired)
        locationAccessAcquired -> stringResource(R.string.credential_settings_acquire_precise_location_access)
        else -> stringResource(R.string.credential_settings_acquire_location_access)
    }
    Button(
        onClick = onClick,
        enabled = !preciseLocationAccessAcquired,
    ) {
        Text(buttonText)
    }
}

@Composable
private fun PeriodicLocationRetrievalSettingsColumn(
    enabled: Boolean,
    periodicLocationRetrievalEnabled: Boolean,
    onPeriodicLocationRetrievalEnabledCheckedChange: (Boolean) -> Unit,
    periodicLocationRetrievalIntervalPreset: PeriodicLocationRetrievalIntervalPreset,
    onPeriodicLocationRetrievalIntervalPresetChanged: (PeriodicLocationRetrievalIntervalPreset) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.location_settings_periodic_location_retrieval_column_title),
            style = InterscheckinTextStyle.ExtraLarge,
        )
        PeriodicLocationRetrievalEnabledSettingsRow(
            enabled = enabled,
            periodicLocationRetrievalEnabled = periodicLocationRetrievalEnabled,
            onPeriodicLocationRetrievalEnabledCheckedChange = onPeriodicLocationRetrievalEnabledCheckedChange,
        )
        CompositionLocalProvider(
            LocalContentColor provides if (enabled && periodicLocationRetrievalEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        ) {
            PeriodicLocationRetrievalIntervalPresetSettingsColumn(
                enabled = enabled && periodicLocationRetrievalEnabled,
                periodicLocationRetrievalIntervalPreset = periodicLocationRetrievalIntervalPreset,
                onPeriodicLocationRetrievalIntervalPresetChanged = onPeriodicLocationRetrievalIntervalPresetChanged,
            )
        }
    }
}

@Composable
private fun PeriodicLocationRetrievalEnabledSettingsRow(
    enabled: Boolean,
    periodicLocationRetrievalEnabled: Boolean,
    onPeriodicLocationRetrievalEnabledCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.location_settings_periodic_location_retrieval_enabled),
            style = InterscheckinTextStyle.Large,
        )
        Switch(
            checked = periodicLocationRetrievalEnabled,
            onCheckedChange = onPeriodicLocationRetrievalEnabledCheckedChange,
            enabled = enabled,
        )
    }
}

@Composable
private fun PeriodicLocationRetrievalIntervalPresetSettingsColumn(
    enabled: Boolean,
    periodicLocationRetrievalIntervalPreset: PeriodicLocationRetrievalIntervalPreset,
    onPeriodicLocationRetrievalIntervalPresetChanged: (PeriodicLocationRetrievalIntervalPreset) -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.location_settings_periodic_location_retrieval_interval_preset_column_title),
            style = InterscheckinTextStyle.ExtraLarge,
        )
        listOf(
            PeriodicLocationRetrievalIntervalPreset.High,
            PeriodicLocationRetrievalIntervalPreset.Medium,
            PeriodicLocationRetrievalIntervalPreset.Low,
        ).forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = it.name(),
                    style = InterscheckinTextStyle.Large,
                )
                RadioButton(
                    selected = periodicLocationRetrievalIntervalPreset == it,
                    onClick = { onPeriodicLocationRetrievalIntervalPresetChanged(it) },
                    enabled = enabled,
                )
            }
        }
    }
}

@Composable
private fun PeriodicLocationRetrievalIntervalPreset.name(): String = when (this) {
    PeriodicLocationRetrievalIntervalPreset.Low -> stringResource(R.string.location_settings_periodic_location_retrieval_interval_preset_low, interval)
    PeriodicLocationRetrievalIntervalPreset.Medium -> stringResource(R.string.location_settings_periodic_location_retrieval_interval_preset_medium, interval)
    PeriodicLocationRetrievalIntervalPreset.High -> stringResource(R.string.location_settings_periodic_location_retrieval_interval_preset_high, interval)
}
