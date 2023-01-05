package pub.yusuke.interscheckin.ui.histories

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject

@HiltAndroidTest
class HistoriesScreenTest {
    val mockkRule = MockKRule(this)
    val hiltRule = HiltAndroidRule(this)
    val composeTestRule = createComposeRule()

    @get:Rule
    val uiTestRule = RuleChain
        .outerRule(mockkRule)
        .around(hiltRule)
        .around(composeTestRule)

    @Inject
    lateinit var vm: HistoriesContract.ViewModel

    @MockK
    lateinit var navController: NavController

    /**
     * See also [FakeHistoriesViewModelModule] for initial setup
     */
    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `A check-in with a venue is displayed on the screen`() {
        composeTestRule.setContent {
            HistoriesScreen(
                viewModel = vm,
                navController = navController
            )
        }

        composeTestRule
            .onAllNodesWithText("test venue 1")
            .assertCountEquals(1)
    }

    @Test
    fun `Display _No Histories Found_ when no check-in history is found`() {
        // given (that no histories are available)
        every { vm.checkinsFlow } returns flowOf(
            PagingData.empty(
                LoadStates(
                    LoadState.NotLoading(true),
                    LoadState.NotLoading(true),
                    LoadState.NotLoading(true)
                )
            )
        )

        composeTestRule.setContent {
            HistoriesScreen(
                viewModel = vm,
                navController = navController
            )
        }

        // then
        composeTestRule
            .onNodeWithContentDescription("No histories found")
            .assertExists()
    }
}
