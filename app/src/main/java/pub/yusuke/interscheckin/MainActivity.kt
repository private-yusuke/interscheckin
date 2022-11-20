package pub.yusuke.interscheckin

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import pub.yusuke.interscheckin.repositories.settings.SettingsRepository
import pub.yusuke.interscheckin.ui.main.MainContract
import pub.yusuke.interscheckin.ui.main.MainScreen
import pub.yusuke.interscheckin.ui.main.MainViewModel
import pub.yusuke.interscheckin.ui.settings.SettingsContract
import pub.yusuke.interscheckin.ui.settings.SettingsScreen
import pub.yusuke.interscheckin.ui.settings.SettingsViewModel
import javax.inject.Inject

private const val LOG_TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLocationPermission()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "main"
            ) {
                composable("main") {
                    val viewModel: MainContract.ViewModel by viewModels<MainViewModel>()

                    MainScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable(
                    "settings/{reasonId}",
                    arguments = listOf(navArgument("reasonId") { type = NavType.ReferenceType })
                ) {
                    val viewModel: SettingsContract.ViewModel = hiltViewModel<SettingsViewModel>()

                    SettingsScreen(
                        viewModel = viewModel,
                        navController = navController,
                        onRecreateRequired = {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                    )
                }
            }
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) && permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                }
                else -> {
                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}
