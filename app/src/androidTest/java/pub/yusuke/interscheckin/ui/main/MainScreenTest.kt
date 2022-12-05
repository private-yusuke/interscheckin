package pub.yusuke.interscheckin.ui.main

import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import pub.yusuke.interscheckin.ui.main.util.MainScreenTestData
import javax.inject.Inject

@HiltAndroidTest
class MainScreenTest {
    val mockkRule = MockKRule(this)
    val hiltRule = HiltAndroidRule(this)
    val composeTestRule = createComposeRule()

    @get:Rule
    val uiTestRule = RuleChain
        .outerRule(mockkRule)
        .around(hiltRule)
        .around(composeTestRule)

    @Module
    @InstallIn(SingletonComponent::class)
    object MainScreenTestModule {
        @Provides
        fun provideViewModel(
            interactor: MainContract.Interactor
        ): MainContract.ViewModel = spyk(MainViewModel(interactor))
    }

    @Inject
    lateinit var vm: MainContract.ViewModel

    @MockK
    lateinit var navController: NavController

    @MockK(relaxUnitFun = true)
    lateinit var vibrator: Vibrator

    @BindValue
    @MockK
    lateinit var vibratorManager: VibratorManager

    @BindValue
    @MockK
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Before
    fun init() {
        MockKAnnotations.init()
        hiltRule.inject()

        every { vibratorManager.defaultVibrator } returns vibrator

        every {
            fusedLocationProviderClient.requestLocationUpdates(
                any(),
                any<LocationCallback>(),
                any()
            )
        } returns mockk()
        every {
            fusedLocationProviderClient.removeLocationUpdates(any<LocationCallback>())
        } returns mockk()

        // Defaults to both the location and the venues loaded
        every { vm.locationState } returns mutableStateOf(MainScreenTestData.locationStateLoaded)
        every { vm.venuesState } returns mutableStateOf(MainScreenTestData.venuesStateIdle)
    }

    @Test
    fun verifyCheckinButtonDisabledWhileUpdatingVenuesList() {
        // when (both the location and the venues not loaded yet)
        every { vm.locationState } returns mutableStateOf(MainScreenTestData.locationStateLoading)
        every { vm.venuesState } returns mutableStateOf(MainScreenTestData.venuesStateLoading)

        composeTestRule.setContent {
            MainScreen(
                viewModel = vm,
                navController = navController
            )
        }

        // then
        composeTestRule
            .onNodeWithContentDescription("Button for creating a Checkin")
            .assertIsNotEnabled()
    }

    @Test
    fun verifyCheckinButtonEnabledAfterUpdatingVenuesList() {
        composeTestRule.setContent {
            MainScreen(
                viewModel = vm,
                navController = navController
            )
        }

        // when
        composeTestRule
            .onNodeWithText("test categories")
            .performClick()

        // then
        composeTestRule
            .onNodeWithContentDescription("Button for creating a Checkin")
            .assertIsEnabled()
    }

    @Test
    fun verifyShoutConsumedAfterCreatingCheckinByClickingButton() {
        composeTestRule.setContent {
            MainScreen(
                viewModel = vm,
                navController = navController
            )
        }

        // given
        val shout = "shout test"
        composeTestRule
            .onNodeWithContentDescription("TextField for shout")
            .performTextInput(shout)

        // when
        composeTestRule
            .onNodeWithText("test categories")
            .performClick()
        composeTestRule
            .onNodeWithContentDescription("Button for creating a Checkin")
            .performClick()
        composeTestRule.waitUntil {
            vm.checkinState.value is MainContract.CheckinState.Idle
        }

        // then
        assert(
            composeTestRule
                .onNodeWithContentDescription("TextField for shout")
                .fetchSemanticsNode()
                .config[SemanticsProperties.EditableText]
                .text
                .isEmpty()
        )
        val checkin = (vm.checkinState.value as MainContract.CheckinState.Idle).lastCheckin
        assert(checkin.shout == shout)
    }

    @Test
    fun verifyShoutConsumedAfterCreatingCheckinByLongClick() {
        composeTestRule.setContent {
            MainScreen(
                viewModel = vm,
                navController = navController
            )
        }

        // given
        val shout = "shout test"
        composeTestRule
            .onNodeWithContentDescription("TextField for shout")
            .performTextInput(shout)

        // when
        composeTestRule
            .onNodeWithText("test venue", substring = true)
            .performTouchInput {
                longClick()
            }
        composeTestRule.waitUntil {
            vm.checkinState.value is MainContract.CheckinState.Idle
        }

        // then
        assert(
            composeTestRule
                .onNodeWithContentDescription("TextField for shout")
                .fetchSemanticsNode()
                .config[SemanticsProperties.EditableText]
                .text
                .isEmpty()
        )
        val checkin = (vm.checkinState.value as MainContract.CheckinState.Idle).lastCheckin
        assert(checkin.shout == shout)
    }
}
