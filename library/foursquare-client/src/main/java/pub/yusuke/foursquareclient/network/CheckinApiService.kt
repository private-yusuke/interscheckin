package pub.yusuke.foursquareclient.network

import pub.yusuke.foursquareclient.models.Checkin
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckinApiService {
    @POST("/v2/checkins/add")
    @Headers("Accept: application/json")
    suspend fun createCheckin(
        @Query("oauth_token")
        oauthToken: String,
        @Query("venueId")
        venueId: String,
        @Query("ll")
        ll: String? = null,
        @Query("shout")
        shout: String? = null,
        @Query("mentions")
        mentions: String?,
        @Query("broadcast")
        broadcast: String?,
        @Query("v")
        v: String? = "20221002",
        @Header("Authorization")
        authorization: String
    ): CreateCheckinResponse

    data class CreateCheckinResponse(
        val meta: CreateCheckinResponseMeta,
        val response: CreateCheckinResponseResponse
    )

    data class CreateCheckinResponseResponse(
        val checkin: Checkin
    )

    data class CreateCheckinResponseMeta(
        val code: String,
        val requestId: String
    )
}
