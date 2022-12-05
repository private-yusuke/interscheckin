package pub.yusuke.interscheckin.ui.main

import android.location.Location
import android.os.VibratorManager
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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

    @BindValue
    @MockK
    lateinit var vibratorManager: VibratorManager

    @BindValue
    @MockK
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Before
    fun init() {
        hiltRule.inject()
        MockKAnnotations.init()

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
    }

    @Test
    fun verifyCheckinButtonDisabledWhileUpdatingVenuesList() {
        // when
        every { vm.locationState } returns mutableStateOf(MainContract.LocationState.Loading())
        every { vm.venuesState } returns mutableStateOf(MainContract.VenuesState.Loading(emptyList()))

        composeTestRule.setContent {
            MainScreen(
                viewModel = vm,
                navController = navController
            )
        }

        // then
        composeTestRule
            .onNodeWithContentDescription("Create a Checkin")
            .assertIsNotEnabled()
    }

    @Test
    fun verifyCheckinButtonEnabledAfterUpdatingVenuesList() {
        // given
        every { vm.locationState } returns mutableStateOf(
            MainContract.LocationState.Loaded(
                Location(
                    ""
                )
            )
        )
        every { vm.venuesState } returns mutableStateOf(
            MainContract.VenuesState.Idle(
                listOf(
                    MainContract.Venue(
                        id = "abc",
                        name = "test venue",
                        categoriesString = "test categories",
                        distance = 1,
                        icon = MainContract.Venue.Icon(
                            name = "icon name",
                            url = "https://example.com/test.icon"
                        )
                    )
                )
            )
        )

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
            .onNodeWithContentDescription("Create a Checkin")
            .assertIsEnabled()
    }

    /*
     * TODO: Venue list 更新中にチェックインしようとしてもクラッシュしないことを確認するためのテストを書く
     * ref: https://github.com/private-yusuke/interscheckin/issues/16
     */
}
