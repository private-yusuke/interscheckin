package pub.yusuke.foursquareclient.network

import com.squareup.moshi.Json
import pub.yusuke.foursquareclient.models.Icon
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
        authorization: String,
    ): SearchPlacesResponse

    data class SearchPlacesResponse(
        val results: List<Venue>,
    )

    /**
     * https://docs.foursquare.com/fsq-developers-places/reference/geotagging-candidates
     */
    @GET("https://places-api.foursquare.com/geotagging/candidates")
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
        @Query("fields")
        fields: String = GEOTAGGING_CANDIDATE_FIELDS,
        @Header("X-Places-Api-Version")
        apiVersion: String = PLACES_API_VERSION,
        @Header("Authorization")
        authorization: String,
    ): SearchPlacesNearbyResponse

    data class SearchPlacesNearbyResponse(
        val candidates: List<GeotaggingCandidate>,
    )

    data class GeotaggingCandidate(
        val categories: List<GeotaggingCandidateCategory>,
        val chains: List<GeotaggingCandidateChain>?,
        @Json(name = "fsq_place_id")
        val fsqPlaceId: String,
        val latitude: Double?,
        val longitude: Double?,
        val link: String?,
        val location: GeotaggingCandidateLocation?,
        val name: String,
    )

    data class GeotaggingCandidateCategory(
        @Json(name = "fsq_category_id")
        val fsqCategoryId: String,
        val icon: Icon,
        val name: String,
    )

    data class GeotaggingCandidateChain(
        @Json(name = "fsq_chain_id")
        val fsqChainId: String,
        val name: String,
    )

    data class GeotaggingCandidateLocation(
        val address: String? = null,
        val country: String? = null,
        @Json(name = "formatted_address")
        val formattedAddress: String? = null,
        val locality: String? = null,
        val postcode: String? = null,
        val region: String? = null,
    )

    /**
     * https://developer.foursquare.com/reference/v2-venues-search
     */
    @Deprecated(
        message = "The corresponding API endpoint is deprecated.",
        replaceWith = ReplaceWith(
            "searchPlacesNearby(...)",
            "com.example.foursquare_client.network.searchPlacesNearby",
        ),
        DeprecationLevel.ERROR,
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
        v: String? = "20221002",
    ): SearchVenuesResponse

    data class SearchVenuesResponse(
        val venues: List<Venue>,
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
        authorization: String,
    ): GetAutocompleteResultsResponse

    data class GetAutocompleteResultsResponse(
        val results: List<GetAutoCompleteResultsResponseItem>,
    )

    data class GetAutoCompleteResultsResponseItem(
        val type: String,
        val place: Venue?,
    )

    companion object {
        const val PLACES_API_VERSION = "2025-06-17"
        const val GEOTAGGING_CANDIDATE_FIELDS =
            "fsq_place_id,name,categories,chains,latitude,longitude,link,location"
    }
}
