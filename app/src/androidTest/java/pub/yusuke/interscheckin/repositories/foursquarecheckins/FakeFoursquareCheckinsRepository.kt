package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.Score
import pub.yusuke.foursquareclient.models.V3Venue
import javax.inject.Inject

class FakeFoursquareCheckinsRepository @Inject constructor() : FoursquareCheckinsRepository {
    override suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double
    ): Checkin =
        Checkin(
            id = "checkin_id",
            shout = shout,
            createdAt = 100,
            type = "checkin",
            timeZoneOffset = 0,
            editableUntil = null,
            isMayor = false,
            score = Score(
                total = 1
            ),
            venue = V3Venue(
                id = venueId,
                name = "good venue"
            )
        )
}
