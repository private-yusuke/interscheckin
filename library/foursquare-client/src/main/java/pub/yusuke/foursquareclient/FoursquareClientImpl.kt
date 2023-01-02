package pub.yusuke.foursquareclient

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.LatAndLong
import pub.yusuke.foursquareclient.models.Venue
import pub.yusuke.foursquareclient.models.llString
import pub.yusuke.foursquareclient.network.CheckinApiService
import pub.yusuke.foursquareclient.network.VenueApiService
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FoursquareClientImpl(
    base_url: String = "https://api.foursquare.com",
    private val oauth_token: String,
    private val api_key: String
) : FoursquareClient {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(base_url)
        .client(client)
        .build()

    private val venueApiService by lazy {
        retrofit.create(VenueApiService::class.java)
    }

    private val checkinApiService by lazy {
        retrofit.create(CheckinApiService::class.java)
    }

    override suspend fun searchPlaces(
        ll: LatAndLong?,
        query: String?,
        radius: Int?,
        limit: Int?,
        categories: String?,
        sort: String?
    ): List<Venue> = try {
        if (api_key.isBlank()) {
            throw EmptyAPIKeyException("Foursquare API key is blank.")
        }
        venueApiService.searchPlaces(
            ll = ll?.llString(),
            query = query,
            radius = radius,
            limit = limit,
            categories = categories,
            sort = sort,
            authorization = api_key
        ).results
    } catch (e: HttpException) {
        if (e.code() == 401) {
            throw FoursquareClient.InvalidRequestTokenException(e.message())
        } else {
            throw e
        }
    }

    override suspend fun searchPlacesNearby(
        ll: LatAndLong,
        hacc: Double?,
        query: String?,
        limit: Int?
    ): List<Venue> =
        try {
            if (api_key.isBlank()) {
                throw EmptyAPIKeyException("Foursquare API key is blank.")
            }
            venueApiService.searchPlacesNearby(
                ll = ll.llString(),
                hacc = hacc,
                query = query,
                limit = limit,
                authorization = api_key
            ).results
        } catch (e: HttpException) {
            if (e.code() == 401) {
                throw FoursquareClient.InvalidRequestTokenException(e.message())
            } else {
                throw e
            }
        }

    @Deprecated(
        message = "The corresponding API endpoint is deprecated.",
        replaceWith = ReplaceWith(
            "searchPlacesNearby(...)",
            "com.example.foursquare_client.FoursquareClientImpl"
        ),
        DeprecationLevel.ERROR
    )
    @Suppress("DEPRECATION_ERROR")
    override suspend fun searchVenues(
        ll: LatAndLong?,
        near: String?,
        radius: Int?,
        query: String?,
        categoryId: String?,
        limit: Int?
    ): List<Venue> = try {
        if (oauth_token.isBlank()) {
            throw EmptyOAuthTokenException("Foursquare OAuth token is blank.")
        }
        venueApiService.searchVenues(
            ll = ll?.llString(),
            near = near,
            radius = radius,
            query = query,
            categoryId = categoryId,
            limit = limit,
            oauthToken = oauth_token
        ).venues
    } catch (e: HttpException) {
        if (e.code() == 401) {
            throw FoursquareClient.InvalidRequestTokenException(e.message())
        } else {
            throw e
        }
    }

    override suspend fun getAutocompleteResults(
        ll: LatAndLong?,
        query: String?,
        radius: Int?,
        limit: Int?,
        sessionToken: String
    ): List<Venue> = try {
        val result = venueApiService.getAutocompleteResults(
            ll = ll?.llString(),
            query = query,
            radius = radius,
            limit = limit,
            sessionToken = sessionToken,
            authorization = api_key
        )
        if (result.results.first().type == "search") {
            emptyList()
        } else {
            result.results.map { it.place!! }
        }
    } catch (e: HttpException) {
        if (e.code() == 401) {
            throw FoursquareClient.InvalidRequestTokenException(e.message())
        } else {
            throw e
        }
    }

    override suspend fun createCheckin(
        venueId: String,
        shout: String?,
        mentions: String?,
        broadcast: String?,
        ll: String
    ): Checkin = coroutineScope {
        try {
            checkinApiService.createCheckin(
                venueId = venueId,
                shout = shout,
                mentions = mentions,
                broadcast = broadcast,
                ll = ll,
                oauthToken = oauth_token,
                authorization = api_key
            ).response.checkin
        } catch (e: HttpException) {
            if (e.code() == 401) {
                throw FoursquareClient.InvalidRequestTokenException(e.message())
            } else {
                throw e
            }
        }
    }

    override suspend fun getUserCheckins(
        userId: Long?,
        offset: Long?,
        beforeTimestamp: Long?,
        limit: Long?
    ): List<Checkin> = coroutineScope {
        try {
            checkinApiService.getCheckinHistories(
                userId = userId?.toString() ?: "self",
                offset = offset,
                limit = limit,
                beforeTimestamp = beforeTimestamp,
                oauthToken = oauth_token,
                authorization = api_key
            ).response.checkins.items
        } catch (e: HttpException) {
            if (e.code() == 401) {
                throw FoursquareClient.InvalidRequestTokenException(e.message())
            } else {
                throw e
            }
        }
    }

    class EmptyOAuthTokenException(message: String) : Exception(message)
    class EmptyAPIKeyException(message: String) : Exception(message)
}
