package pub.yusuke.interscheckin.ui.resetsettings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme
import pub.yusuke.interscheckin.ui.utils.InterscheckinTopBar

@Composable
fun ResetSettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ResetSettingsContract.ViewModel = hiltViewModel<ResetSettingsViewModel>(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    InterscheckinTheme {
        Scaffold(
            topBar = {
                InterscheckinTopBar(
                    titleText = stringResource(R.string.reset_settings_topbar_title),
                    onClickNavigationIcon = { navController.popBackStack() },
                )
            },
            modifier = modifier,
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.resetCachedVenues()
                            showToastForResettingDone(context)
                        }
                    },
                ) {
                    Text(stringResource(R.string.reset_settings_reset_cached_venues))
                }
            }
        }
    }
}

private fun showToastForResettingDone(
    context: Context,
) {
    Toast.makeText(
        context,
        context.resources.getString(R.string.reset_settings_successfully_reset_cached_venues),
        Toast.LENGTH_SHORT,
    ).show()
}
