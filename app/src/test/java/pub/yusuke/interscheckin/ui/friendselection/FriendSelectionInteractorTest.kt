package pub.yusuke.interscheckin.ui.friendselection

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.foursquareclient.models.Friend as ApiFriend

class FriendSelectionInteractorTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var foursquareClient: FoursquareClient

    private lateinit var interactor: FriendSelectionInteractor

    private val sampleApiFriends = listOf(
        ApiFriend(
            id = "friend1",
            firstName = "太郎",
            lastName = "田中",
            bio = "こんにちは！",
            photo = ApiFriend.Photo(
                prefix = "https://example.com/photo_",
                suffix = ".jpg",
            ),
            homeCity = "東京, 日本",
        ),
        ApiFriend(
            id = "friend2",
            firstName = "花子",
            lastName = "佐藤",
            bio = null,
            photo = null,
            homeCity = "大阪, 日本",
        ),
    )

    @Test
    fun `fetchFriends returns friends from FoursquareClient`() = runTest {
        // Given
        interactor = FriendSelectionInteractor(foursquareClient)
        coEvery { foursquareClient.getFriends() } returns sampleApiFriends

        // When
        val result = interactor.fetchFriends()

        // Then
        assertEquals(2, result.size)
        assertEquals("friend1", result[0].id)
        assertEquals("太郎", result[0].firstName)
        assertEquals("田中", result[0].lastName)
        coVerify { foursquareClient.getFriends() }
    }

    @Test(expected = RuntimeException::class)
    fun `fetchFriends propagates exception from FoursquareClient`() = runTest {
        // Given
        interactor = FriendSelectionInteractor(foursquareClient)
        coEvery { foursquareClient.getFriends() } throws RuntimeException("Network error")

        // When
        interactor.fetchFriends()

        // Then - exception should be propagated
    }
}
