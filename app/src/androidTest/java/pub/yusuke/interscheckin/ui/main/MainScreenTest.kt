package pub.yusuke.interscheckin.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainScreenTest {
    @get:Rule(order = 0)
    val mockkRule = MockKRule(this)

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createComposeRule()

    @MockK(relaxUnitFun = true)
    lateinit var vm: MainContract.ViewModel

    @MockK
    lateinit var navController: NavController

    @Before
    fun init() {
        // TODO: テスト時には実際の外部 API を叩かせないようにモックを作成して注入するようにする
        hiltRule.inject()

        every { vm.drivingModeFlow } returns flowOf(true)
        every { vm.navigationRequiredState } returns mutableStateOf(null)
        every { vm.snackbarMessageState } returns mutableStateOf(0)
        every { vm.venuesState } returns mutableStateOf(MainContract.VenuesState.Idle(emptyList()))
        every { vm.locationState } returns mutableStateOf(MainContract.LocationState.Loading())
        every { vm.checkinState } returns mutableStateOf(MainContract.CheckinState.InitialIdle)

        composeTestRule.setContent {
            MainScreen(
                viewModel = vm,
                navController = navController
            )
        }
    }

    @Test
    fun verifyCheckinButtonCannotBePressedWhileUpdatingVenuesList() {
        every { vm.locationState } returns mutableStateOf(MainContract.LocationState.Loading())
        every { vm.venuesState } returns mutableStateOf(MainContract.VenuesState.Loading(emptyList()))

        composeTestRule
            .onNodeWithContentDescription("Create a Checkin")
            .assertIsNotEnabled()
    }

    /*
     * TODO: Venue list 更新中にチェックインしようとしてもクラッシュしないことを確認するためのテストを書く
     * ref: https://github.com/private-yusuke/interscheckin/issues/16
     */
}
