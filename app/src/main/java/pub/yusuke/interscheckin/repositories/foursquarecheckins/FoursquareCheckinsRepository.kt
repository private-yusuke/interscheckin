package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.models.Checkin

interface FoursquareCheckinsRepository {
    suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double
    ): Checkin

    suspend fun getCheckins(
        beforeTimestamp: Long? = null,
        limit: Long? = null,
    ): List<Checkin>
}
