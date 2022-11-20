package pub.yusuke.foursquareclient.network

import pub.yusuke.foursquareclient.models.Venue
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface VenueApiService {
    /**
     * https://developer.foursquare.com/reference/place-search
     */
    @GET("/v3/places/search")
    @Headers("Accept: application/json")
    suspend fun searchPlaces(
        @Query("ll")
        ll: String? = null,
        @Query("radius")
        radius: Int? = null,
        @Query("query")
        query: String? = null,
        @Query("categories")
        categories: String? = null,
        @Query("sort")
        sort: String? = null,
        @Query("limit")
        limit: Int? = null,
        @Header("Authorization")
        authorization: String
    ): SearchPlacesResponse

    data class SearchPlacesResponse(
        val results: List<Venue>
    )

    /**
     * https://developer.foursquare.com/reference/places-nearby
     */
    @GET("/v3/places/nearby")
    @Headers("Accept: application/json")
    suspend fun searchPlacesNearby(
        @Query("ll")
        ll: String? = null,
        @Query("hacc")
        hacc: Double? = null,
        @Query("query")
        query: String? = null,
        @Query("limit")
        limit: Int? = null,
        @Header("Authorization")
        authorization: String
    ): SearchPlacesNearbyResponse

    data class SearchPlacesNearbyResponse(
        val results: List<Venue>
    )

    /**
     * https://developer.foursquare.com/reference/v2-venues-search
     */
    @Deprecated(
        message = "The corresponding API endpoint is deprecated.",
        replaceWith = ReplaceWith(
            "searchPlacesNearby(...)",
            "com.example.foursquare_client.network.searchPlacesNearby"
        ),
        DeprecationLevel.ERROR
    )
    @GET("/v2/venues/search")
    @Headers("Accept: application/json")
    suspend fun searchVenues(
        // / near or ll must be specified
        @Query("ll")
        ll: String? = null,
        // / near or ll must be specified
        @Query("near")
        near: String? = null,
        @Query("radius")
        radius: Int? = null,
        @Query("query")
        query: String? = null,
        @Query("categoryId")
        categoryId: String? = null,
        // / up to 50
        @Query("limit")
        limit: Int? = null,
        @Query("oauth_token")
        oauthToken: String,
        @Query("v")
        v: String? = "20221002"
    ): SearchVenuesResponse

    data class SearchVenuesResponse(
        val venues: List<Venue>
    )

    @GET("/v3/autocomplete")
    @Headers("Accept: application/json")
    suspend fun getAutocompleteResults(
        @Query("ll")
        ll: String? = null,
        @Query("radius")
        radius: Int? = null,
        @Query("query")
        query: String? = null,
        @Query("limit")
        limit: Int? = null,
        @Query("session_token")
        sessionToken: String,
        @Header("Authorization")
        authorization: String
    ): GetAutocompleteResultsResponse

    data class GetAutocompleteResultsResponse(
        val results: List<GetAutoCompleteResultsResponseItem>
    )

    data class GetAutoCompleteResultsResponseItem(
        val type: String,
        val place: Venue?
    )
}
