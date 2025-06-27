package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.models.Checkin

interface FoursquareCheckinsRepository {
    suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double,
        broadcast: String? = null,
        with: String? = null,
    ): Checkin

    suspend fun getCheckins(
        userId: Long? = null,
        offset: Long? = null,
        beforeTimestamp: Long? = null,
        limit: Long? = null,
    ): List<Checkin>
}
