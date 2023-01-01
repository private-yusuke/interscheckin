package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.FoursquareClient
import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.LatAndLong
import pub.yusuke.foursquareclient.models.llString

class FoursquareCheckinsRepositoryImpl(
    private val foursquareClient: FoursquareClient
) : FoursquareCheckinsRepository {
    override suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double
    ): Checkin =
        foursquareClient.createCheckin(
            venueId = venueId,
            shout = shout,
            ll = LatAndLong(
                latitude = latitude,
                longitude = longitude
            ).llString()
        )

    override suspend fun getCheckins(
        userId: Long?,
        offset: Long?,
        beforeTimestamp: Long?,
        limit: Long?
    ): List<Checkin> =
        foursquareClient.getUserCheckins(
            userId = userId,
            offset = offset,
            beforeTimestamp = beforeTimestamp,
            limit = limit
        )
}
