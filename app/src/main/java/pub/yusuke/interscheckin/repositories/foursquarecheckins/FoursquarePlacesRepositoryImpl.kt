package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.foursquareclient.models.LatAndLong
import pub.yusuke.foursquareclient.models.Venue

class FoursquarePlacesRepositoryImpl(
    private val foursquareClient: FoursquareClient,
) : FoursquarePlacesRepository {
    override suspend fun searchPlacesNearby(
        latitude: Double,
        longitude: Double,
        hacc: Double?,
        limit: Int?,
        query: String?,
    ): List<Venue> =
        foursquareClient.searchPlacesNearby(
            ll = LatAndLong(
                latitude = latitude,
                longitude = longitude,
            ),
            hacc = hacc,
            limit = limit,
            query = query,
        )
}
