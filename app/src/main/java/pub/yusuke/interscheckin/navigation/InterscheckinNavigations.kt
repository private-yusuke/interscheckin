package pub.yusuke.interscheckin.navigation

import android.app.Activity
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pub.yusuke.interscheckin.MainActivity
import pub.yusuke.interscheckin.ui.histories.HistoriesScreen
import pub.yusuke.interscheckin.ui.locationaccessacquirement.LocationAccessAcquirementScreen
import pub.yusuke.interscheckin.ui.main.MainScreen
import pub.yusuke.interscheckin.ui.settings.SettingsScreen
import pub.yusuke.interscheckin.ui.splash.SplashScreen

// Composable な関数の中で Composable な関数っぽく呼び出されているので Supress
@Suppress("FunctionNaming")
fun NavGraphBuilder.InterscheckinNavigations(
    navController: NavController,
    activity: Activity,
) {
    composable(InterscheckinScreens.Splash.route) {
        SplashScreen(navController = navController)
    }

    composable(InterscheckinScreens.LocationAccessAcquirement.route) {
        LocationAccessAcquirementScreen(navController = navController)
    }

    composable(InterscheckinScreens.Main.route) {
        MainScreen(
            navController = navController,
        )
    }
    composable(
        InterscheckinScreens.Settings.route,
        arguments = InterscheckinScreens.Settings.navArguments,
    ) {
        SettingsScreen(
            navController = navController,
            onRecreateRequired = {
                val intent = Intent(activity.applicationContext, MainActivity::class.java)
                activity.startActivity(intent)
                activity.finishAffinity()
            },
        )
    }
    composable(InterscheckinScreens.Histories.route) {
        HistoriesScreen(
            navController = navController,
        )
    }
}
