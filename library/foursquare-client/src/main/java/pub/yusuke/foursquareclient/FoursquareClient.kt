package pub.yusuke.foursquareclient

import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.LatAndLong
import pub.yusuke.foursquareclient.models.Venue

interface FoursquareClient {
    /**
     * This function is unused because [searchPlacesNearby] is preferred by me.
     */
    suspend fun searchPlaces(
        ll: LatAndLong? = null,
        query: String? = null,
        radius: Int? = null,
        limit: Int? = null,
        categories: String? = null,
        sort: String? = null
    ): List<Venue>

    /**
     * Retrieves the Venues around the given position [ll].
     * Calls an V3 API endpoint in the implementation.
     */
    suspend fun searchPlacesNearby(
        ll: LatAndLong,
        hacc: Double? = null,
        query: String? = null,
        limit: Int? = null
    ): List<Venue>

    /**
     * This function is unused because of v2 API endpoints are not available.
     * @see FoursquareClientImpl.searchVenues
     */
    suspend fun searchVenues(
        ll: LatAndLong? = null,
        near: String? = null,
        radius: Int? = null,
        query: String? = null,
        categoryId: String? = null,
        limit: Int? = null
    ): List<Venue>

    /**
     * This function is unused because [searchPlacesNearby] is preferred by me.
     */
    suspend fun getAutocompleteResults(
        ll: LatAndLong? = null,
        query: String? = null,
        radius: Int? = null,
        limit: Int? = null,
        sessionToken: String
    ): List<Venue>

    /**
     * Creates a new check-in at the Venue with [venueId].
     * Calls an V2 API endpoint in the implementation.
     *
     * @param shout An comment for the check-in.
     */
    suspend fun createCheckin(
        venueId: String,
        shout: String? = null,
        mentions: String? = null,
        broadcast: String? = null,
        ll: String
    ): Checkin

    /**
     * Retrieves the check-ins created by the user whose id is [userId].
     * Calls an V2 API endpoint in the implementation.
     * if [userId] is null, the result will be the ones that is created by the logged-in user.
     *
     * @param beforeTimestamp if specified, returns the check-ins created before it. Expects an UNIX timestamp.
     */
    suspend fun getUserCheckins(
        userId: Long? = null,
        offset: Long? = null,
        beforeTimestamp: Long? = null,
        limit: Long? = null
    ): List<Checkin>

    class InvalidRequestTokenException(message: String) : Exception(message)
}
