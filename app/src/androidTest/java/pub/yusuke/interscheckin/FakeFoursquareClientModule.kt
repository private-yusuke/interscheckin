package pub.yusuke.interscheckin

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.mockk
import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.foursquareclient.models.Friend
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FakeFoursquareClientModule {
    @Provides
    @Singleton
    fun provideFoursquareClient(): FoursquareClient {
        val client = mockk<FoursquareClient>()
        
        val sampleFriends = listOf(
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
        
        coEvery { client.getFriends() } returns sampleFriends
        
        return client
    }
}