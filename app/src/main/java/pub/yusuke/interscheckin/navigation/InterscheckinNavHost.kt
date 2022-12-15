package pub.yusuke.interscheckin.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun InterscheckinNavHost(
    navController: NavHostController,
    activity: Activity
) {
    NavHost(
        navController = navController,
        startDestination = InterscheckinScreens.Main.name
    ) {
        InterscheckinNavigations(
            navController = navController,
            activity = activity
        )
    }
}
