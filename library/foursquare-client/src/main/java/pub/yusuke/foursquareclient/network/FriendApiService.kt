package pub.yusuke.foursquareclient.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FriendApiService {
    @GET("/v2/users/self/friends")
    @Headers("Accept: application/json")
    suspend fun getFriends(
        @Query("oauth_token")
        oauthToken: String,
        @Query("limit")
        limit: Int? = null,
        @Query("offset")
        offset: Int? = null,
        @Query("v")
        v: String? = "20221002",
    ): GetFriendsResponse

    data class GetFriendsResponse(
        val meta: Meta,
        val response: Response,
    ) {
        data class Meta(
            val code: Int,
            val requestId: String,
        )

        data class Response(
            val friends: Friends,
        ) {
            data class Friends(
                val count: Int,
                val items: List<Friend>,
            )
        }

        data class Friend(
            val id: String,
            val firstName: String,
            val lastName: String?,
            val bio: String?,
            val photo: Photo?,
            val homeCity: String?,
        ) {
            data class Photo(
                val prefix: String,
                val suffix: String,
            )
        }
    }
}
