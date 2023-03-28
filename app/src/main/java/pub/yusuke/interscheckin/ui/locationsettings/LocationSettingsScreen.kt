package pub.yusuke.interscheckin.ui.locationsettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
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
) {
    val locationPermissionState = rememberLocationAccessAcquirementState()

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding.copy(top = 16.dp)),
            ) {
                LocationAccessAcquirementScreenButton(
                    locationAccessAcquired = locationPermissionState.locationAccessAcquired(),
                    preciseLocationAccessAcquired = locationPermissionState.preciseLocationAccessAcquired(),
                    onClick = {
                        navController.navigate(InterscheckinScreens.LocationAccessAcquirement.route)
                    },
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
