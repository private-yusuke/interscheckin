package pub.yusuke.foursquareclient

import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.LatAndLong
import pub.yusuke.foursquareclient.models.Venue

interface FoursquareClient {
    suspend fun searchPlaces(
        ll: LatAndLong? = null,
        query: String? = null,
        radius: Int? = null,
        limit: Int? = null,
        categories: String? = null,
        sort: String? = null
    ): List<Venue>

    suspend fun searchPlacesNearby(
        ll: LatAndLong,
        hacc: Double? = null,
        query: String? = null,
        limit: Int? = null
    ): List<Venue>

    suspend fun searchVenues(
        ll: LatAndLong? = null,
        near: String? = null,
        radius: Int? = null,
        query: String? = null,
        categoryId: String? = null,
        limit: Int? = null
    ): List<Venue>

    suspend fun getAutocompleteResults(
        ll: LatAndLong? = null,
        query: String? = null,
        radius: Int? = null,
        limit: Int? = null,
        sessionToken: String
    ): List<Venue>

    suspend fun createCheckin(
        venueId: String,
        shout: String? = null,
        mentions: String? = null,
        broadcast: String? = null,
        ll: String
    ): Checkin

    class InvalidRequestTokenException(message: String) : Exception(message)
}
