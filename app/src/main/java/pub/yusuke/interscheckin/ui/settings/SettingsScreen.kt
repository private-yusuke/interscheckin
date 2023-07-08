package pub.yusuke.interscheckin.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
import pub.yusuke.interscheckin.ui.theme.InterscheckinTextStyle
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme
import pub.yusuke.interscheckin.ui.utils.InterscheckinTopBar

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    InterscheckinTheme {
        Scaffold(
            topBar = {
                InterscheckinTopBar(
                    titleText = stringResource(R.string.settings_topbar_title),
                    onClickNavigationIcon = { navController.popBackStack() },
                )
            },
            modifier = modifier,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
            ) {
                NavigationRow(
                    title = stringResource(R.string.settings_credential_settings),
                    onClick = { navController.navigate(InterscheckinScreens.CredentialSettings.route) },
                )
                NavigationRow(
                    title = stringResource(R.string.settings_location_settings),
                    onClick = { navController.navigate(InterscheckinScreens.LocationSettings.route) },
                )
                NavigationRow(
                    title = stringResource(R.string.settings_reset_settings),
                    onClick = { navController.navigate(InterscheckinScreens.ResetSettings.route) },
                )
            }
        }
    }
}

@Composable
private fun NavigationRow(
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            text = title,
            style = InterscheckinTextStyle.Large,
        )
    }
}
