package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.models.Venue

interface FoursquarePlacesRepository {
    suspend fun searchPlacesNearby(
        latitude: Double,
        longitude: Double,
        hacc: Double?,
        limit: Int? = null,
        query: String? = null,
    ): List<Venue>
}
