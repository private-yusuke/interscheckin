package pub.yusuke.interscheckin.ui.locationaccessacquirement

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
import pub.yusuke.interscheckin.ui.theme.InterscheckinPrimaryButton
import pub.yusuke.interscheckin.ui.theme.InterscheckinSecondaryButton
import pub.yusuke.interscheckin.ui.theme.InterscheckinTextStyle
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationAccessAcquirementScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LocationAccessAcquirementContract.ViewModel = hiltViewModel<LocationAccessAcquirementViewModel>(),
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    var screenState: LocationAccessAcquirementContract.ScreenState by remember {
        mutableStateOf(
            LocationAccessAcquirementContract.ScreenState.Loading,
        )
    }
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    ) {
        screenState = computeScreenState(
            it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false),
            it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false),
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION),
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION),
        )
    }
    val locationAccessAcquired = locationPermissionState
        .permissions
        .any {
            it.permission == Manifest.permission.ACCESS_COARSE_LOCATION &&
                it.status == PermissionStatus.Granted
        }
    val preciseLocationAccessAcquired = locationPermissionState
        .allPermissionsGranted
    val shouldShowRationaleForPreciseLocationAcquirement =
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    val shouldShowRationaleForLocationAcquirement =
        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

    screenState = computeScreenState(
        locationAccessAcquired,
        preciseLocationAccessAcquired,
        shouldShowRationaleForLocationAcquirement,
        shouldShowRationaleForPreciseLocationAcquirement,
    )

    InterscheckinTheme {
        Surface(
            modifier = modifier,
        ) {
            when (screenState) {
                LocationAccessAcquirementContract.ScreenState.Loading ->
                    LoadingContent()

                LocationAccessAcquirementContract.ScreenState.NoLocationAccessAcquired ->
                    NoLocationAccessAcquiredContent(
                        onAffirmativeRequest = {
                            locationPermissionState.launchMultiplePermissionRequest()
                        },
                        onNegativeRequest = { navController.jumpToMain() },
                    )

                LocationAccessAcquirementContract.ScreenState.OnlyCoarseLocationAccessAcquired ->
                    OnlyCoarseLocationAccessAcquiredContent(
                        onAffirmativeRequest = {
                            locationPermissionState.launchMultiplePermissionRequest()
                        },
                        onNegativeRequest = { navController.jumpToMain() },
                    )

                LocationAccessAcquirementContract.ScreenState.PreciseLocationAccessAcquired ->
                    PreciseLocationAccessAcquiredContent(
                        onAffirmativeRequest = { navController.jumpToMain() },
                    )

                LocationAccessAcquirementContract.ScreenState.LocationAccessNotAcquirable ->
                    LocationAccessNotAcquirableContent(
                        onAffirmativeRequest = { openApplicationDetailsSettings(context) },
                        onNegativeRequest = { navController.jumpToMain() },
                    )

                LocationAccessAcquirementContract.ScreenState.PreciseLocationAccessNotAcquirable ->
                    PreciseLocationAccessNotAcquirableContent(
                        onAffirmativeRequest = { openApplicationDetailsSettings(context) },
                        onNegativeRequest = { navController.jumpToMain() },
                    )
            }
        }
    }

    viewModel.onScreenRendered()
}

private fun computeScreenState(
    locationAccessAcquired: Boolean,
    preciseLocationAccessAcquired: Boolean,
    shouldShowRationaleForLocationAcquirement: Boolean,
    shouldShowRationaleForPreciseLocationAcquirement: Boolean,
): LocationAccessAcquirementContract.ScreenState {
    return when {
        !locationAccessAcquired && shouldShowRationaleForLocationAcquirement ->
            LocationAccessAcquirementContract.ScreenState.NoLocationAccessAcquired

        !preciseLocationAccessAcquired && shouldShowRationaleForPreciseLocationAcquirement ->
            LocationAccessAcquirementContract.ScreenState.OnlyCoarseLocationAccessAcquired

        preciseLocationAccessAcquired ->
            LocationAccessAcquirementContract.ScreenState.PreciseLocationAccessAcquired

        !locationAccessAcquired ->
            LocationAccessAcquirementContract.ScreenState.LocationAccessNotAcquirable

        else ->
            LocationAccessAcquirementContract.ScreenState.PreciseLocationAccessNotAcquirable
    }
}

/**
 * Should be unreachable
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun NoLocationAccessAcquiredContent(
    onAffirmativeRequest: () -> Unit,
    onNegativeRequest: () -> Unit,
) {
    TwoChoicesContent(
        title = stringResource(R.string.location_access_acquirement_no_location_access_acquired_content_title),
        description = stringResource(R.string.location_access_acquirement_no_location_access_acquired_content_description),
        affirmativeActionText = stringResource(R.string.location_access_acquirement_no_location_access_acquired_content_allow),
        negativeActionText = stringResource(R.string.location_access_acquirement_no_location_access_acquired_content_deny),
        onAffirmativeRequest = onAffirmativeRequest,
        onNegativeRequest = onNegativeRequest,
    )
}

@Composable
private fun OnlyCoarseLocationAccessAcquiredContent(
    onAffirmativeRequest: () -> Unit,
    onNegativeRequest: () -> Unit,
) {
    TwoChoicesContent(
        title = stringResource(R.string.location_access_acquirement_only_coarse_location_access_acquired_content_title),
        description = stringResource(R.string.location_access_acquirement_only_coarse_location_access_acquired_content_description),
        affirmativeActionText = stringResource(R.string.location_access_acquirement_only_coarse_location_access_acquired_content_allow),
        negativeActionText = stringResource(R.string.location_access_acquirement_only_coarse_location_access_acquired_content_deny),
        onAffirmativeRequest = onAffirmativeRequest,
        onNegativeRequest = onNegativeRequest,
    )
}

@Composable
private fun PreciseLocationAccessAcquiredContent(
    onAffirmativeRequest: () -> Unit,
) {
    TwoChoicesContent(
        title = stringResource(R.string.location_access_acquirement_precise_location_access_acquired_content_title),
        description = stringResource(R.string.location_access_acquirement_precise_location_access_acquired_content_description),
        affirmativeActionText = stringResource(R.string.location_access_acquirement_precise_location_access_acquired_content_close),
        negativeActionText = null,
        onAffirmativeRequest = onAffirmativeRequest,
        onNegativeRequest = null,
    )
}

@Composable
private fun PreciseLocationAccessNotAcquirableContent(
    onAffirmativeRequest: () -> Unit,
    onNegativeRequest: () -> Unit,
) {
    TwoChoicesContent(
        title = stringResource(R.string.location_access_acquirement_precise_location_access_not_acquirable_content_title),
        description = stringResource(R.string.location_access_acquirement_precise_location_access_not_acquirable_content_description),
        affirmativeActionText = stringResource(R.string.location_access_acquirement_precise_location_access_not_acquirable_content_settings),
        negativeActionText = stringResource(R.string.location_access_acquirement_precise_location_access_not_acquirable_content_close),
        onAffirmativeRequest = onAffirmativeRequest,
        onNegativeRequest = onNegativeRequest,
    )
}

@Composable
private fun LocationAccessNotAcquirableContent(
    onAffirmativeRequest: () -> Unit,
    onNegativeRequest: () -> Unit,
) {
    TwoChoicesContent(
        title = stringResource(R.string.location_access_acquirement_location_access_not_acquirable_content_title),
        description = stringResource(R.string.location_access_acquirement_location_access_not_acquirable_content_description),
        affirmativeActionText = stringResource(R.string.location_access_acquirement_location_access_not_acquirable_content_settings),
        negativeActionText = stringResource(R.string.location_access_acquirement_location_access_not_acquirable_content_close),
        onAffirmativeRequest = onAffirmativeRequest,
        onNegativeRequest = onNegativeRequest,
    )
}

@Composable
private fun TwoChoicesContent(
    title: String,
    description: String,
    affirmativeActionText: String?,
    negativeActionText: String?,
    onAffirmativeRequest: (() -> Unit)?,
    onNegativeRequest: (() -> Unit)?,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = title,
                style = InterscheckinTextStyle.SuperExtraLarge,
            )
            Divider()
            Text(
                text = description,
                style = InterscheckinTextStyle.Normal,
            )
        }
        Row(
            horizontalArrangement = twoSidesArrangement(
                startVisible = onAffirmativeRequest != null,
                endVisible = onNegativeRequest != null,
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            onNegativeRequest?.let {
                InterscheckinSecondaryButton(
                    text = requireNotNull(negativeActionText),
                    onClick = it,
                )
            }

            onAffirmativeRequest?.let {
                InterscheckinPrimaryButton(
                    text = requireNotNull(affirmativeActionText),
                    onClick = it,
                )
            }
        }
    }
}

private fun twoSidesArrangement(
    startVisible: Boolean,
    endVisible: Boolean,
) =
    if (startVisible) {
        if (endVisible) {
            Arrangement.SpaceBetween
        } else {
            Arrangement.Start
        }
    } else {
        Arrangement.End
    }

private fun NavController.jumpToMain() {
    navigate(InterscheckinScreens.Main.route) {
        popUpTo(InterscheckinScreens.LocationAccessAcquirement.route) {
            inclusive = true
        }
    }
}

private fun openApplicationDetailsSettings(
    context: Context,
) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

private fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    error("Couldn't find Activity from the given context")
}

@Preview
@Composable
private fun PreviewTwoChoicesContent() {
    TwoChoicesContent(
        title = "test",
        description = "this is a test",
        affirmativeActionText = "Submit",
        negativeActionText = "Dismiss",
        onAffirmativeRequest = {},
        onNegativeRequest = {},
    )
}
