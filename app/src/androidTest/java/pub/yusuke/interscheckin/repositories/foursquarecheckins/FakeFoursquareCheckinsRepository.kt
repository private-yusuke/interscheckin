package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.Score
import pub.yusuke.foursquareclient.models.V2Venue
import javax.inject.Inject

class FakeFoursquareCheckinsRepository @Inject constructor() : FoursquareCheckinsRepository {
    private val exampleCheckin = Checkin(
        id = "checkin_id",
        shout = "hey",
        createdAt = 100,
        type = "checkin",
        timeZoneOffset = 0,
        editableUntil = null,
        isMayor = false,
        score = Score(
            total = 1
        ),
        venue = V2Venue(
            id = "venue_id",
            name = "good venue"
        )
    )

    override suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double
    ): Checkin = exampleCheckin.copy(
        shout = shout,
        venue = exampleCheckin.venue.copy(
            id = venueId
        )
    )

    override suspend fun getCheckins(
        beforeTimestamp: Long?,
        limit: Long?
    ): List<Checkin> =
        listOf(exampleCheckin)
}
