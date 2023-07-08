package pub.yusuke.interscheckin.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
import pub.yusuke.interscheckin.ui.theme.InterscheckinTextStyle
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: SplashContract.ViewModel = hiltViewModel<SplashViewModel>(),
) {
    val nextInterscheckinScreen by viewModel.nextInterscheckinScreenStateFlow.collectAsState()

    nextInterscheckinScreen?.let {
        navController.navigate(it.route) {
            popUpTo(InterscheckinScreens.Splash.route) {
                inclusive = true
            }
        }
        viewModel.onNavigatedToAnotherScreen()
    }

    InterscheckinTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier,
        ) {
            Text(
                text = stringResource(R.string.splash_interscheckin_title),
                style = InterscheckinTextStyle.SuperExtraLarge,
            )
            Text(
                text = stringResource(R.string.splash_interscheckin_loading),
                style = InterscheckinTextStyle.Normal,
            )
        }
    }
}
