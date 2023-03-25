package pub.yusuke.interscheckin.ui.utils

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun rememberLocationAccessAcquirementState(
    onPermissionsResult: (Map<String, Boolean>) -> Unit = {},
) =
    rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = onPermissionsResult,
    )

@ExperimentalPermissionsApi
fun MultiplePermissionsState.locationAccessAcquired() =
    permissions
        .any {
            it.permission == Manifest.permission.ACCESS_COARSE_LOCATION &&
                it.status == PermissionStatus.Granted
        }

@ExperimentalPermissionsApi
fun MultiplePermissionsState.preciseLocationAccessAcquired() =
    permissions
        .any {
            it.permission == Manifest.permission.ACCESS_FINE_LOCATION &&
                it.status == PermissionStatus.Granted
        }
