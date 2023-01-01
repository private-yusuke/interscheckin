package pub.yusuke.interscheckin.navigation

import android.app.Activity
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pub.yusuke.interscheckin.MainActivity
import pub.yusuke.interscheckin.ui.histories.HistoriesScreen
import pub.yusuke.interscheckin.ui.main.MainScreen
import pub.yusuke.interscheckin.ui.settings.SettingsScreen

fun NavGraphBuilder.InterscheckinNavigations(
    navController: NavController,
    activity: Activity
) {
    composable(InterscheckinScreens.Main.route) {
        MainScreen(
            navController = navController
        )
    }
    composable(
        InterscheckinScreens.Settings.route,
        arguments = InterscheckinScreens.Settings.navArguments
    ) {
        SettingsScreen(
            navController = navController,
            onRecreateRequired = {
                val intent = Intent(activity.applicationContext, MainActivity::class.java)
                activity.startActivity(intent)
                activity.finishAffinity()
            }
        )
    }
    composable(InterscheckinScreens.Histories.route) {
        HistoriesScreen(
            navController = navController
        )
    }
}
