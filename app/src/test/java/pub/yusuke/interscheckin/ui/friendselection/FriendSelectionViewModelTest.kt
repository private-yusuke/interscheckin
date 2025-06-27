package pub.yusuke.interscheckin.ui.friendselection

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pub.yusuke.interscheckin.navigation.entity.Friend

@ExperimentalCoroutinesApi
class FriendSelectionViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var interactor: FriendSelectionContract.Interactor

    private lateinit var viewModel: FriendSelectionViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val sampleFriends = listOf(
        Friend(
            id = "friend1",
            firstName = "太郎",
            lastName = "田中",
            bio = "こんにちは！",
            photo = Friend.Photo(
                prefix = "https://example.com/photo_",
                suffix = ".jpg",
            ),
            homeCity = "東京, 日本",
        ),
        Friend(
            id = "friend2",
            firstName = "花子",
            lastName = "佐藤",
            bio = null,
            photo = null,
            homeCity = "大阪, 日本",
        ),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FriendSelectionViewModel(interactor)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadFriends sets state to Loading then Loaded when successful`() = testScope.runTest {
        // Given
        coEvery { interactor.fetchFriends() } returns sampleFriends

        // When
        viewModel.loadFriends()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.friendsState.value
        assertTrue(state is FriendSelectionContract.FriendsState.Loaded)
        assertEquals(sampleFriends.toImmutableList(), (state as FriendSelectionContract.FriendsState.Loaded).friends)
    }

    @Test
    fun `loadFriends sets state to Loading then Error when exception occurs`() = testScope.runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { interactor.fetchFriends() } throws exception

        // When
        viewModel.loadFriends()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.friendsState.value
        assertTrue(state is FriendSelectionContract.FriendsState.Error)
        assertEquals(exception, (state as FriendSelectionContract.FriendsState.Error).throwable)
    }

    @Test
    fun `toggleFriendSelection adds friend when not selected`() = testScope.runTest {
        // Given
        val friend = sampleFriends[0]
        val initialSelection = persistentListOf<Friend>()
        viewModel.setInitialSelectedFriends(initialSelection)

        // When
        viewModel.toggleFriendSelection(friend)

        // Then
        val selectedFriends = viewModel.selectedFriends.value
        assertTrue(selectedFriends.contains(friend))
        assertEquals(1, selectedFriends.size)
    }

    @Test
    fun `toggleFriendSelection removes friend when already selected`() = testScope.runTest {
        // Given
        val friend = sampleFriends[0]
        val initialSelection = listOf(friend)
        viewModel.setInitialSelectedFriends(initialSelection)

        // When
        viewModel.toggleFriendSelection(friend)

        // Then
        val selectedFriends = viewModel.selectedFriends.value
        assertTrue(!selectedFriends.contains(friend))
        assertEquals(0, selectedFriends.size)
    }

    @Test
    fun `setInitialSelectedFriends updates selectedFriends state`() = testScope.runTest {
        // Given
        val friends = sampleFriends

        // When
        viewModel.setInitialSelectedFriends(friends)

        // Then
        val selectedFriends = viewModel.selectedFriends.value
        assertEquals(friends.toImmutableList(), selectedFriends)
    }

    @Test
    fun `fetchFriends is called during initialization`() = testScope.runTest {
        // Given
        coEvery { interactor.fetchFriends() } returns sampleFriends

        // When (ViewModel is already initialized in setup)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { interactor.fetchFriends() }
    }
}
