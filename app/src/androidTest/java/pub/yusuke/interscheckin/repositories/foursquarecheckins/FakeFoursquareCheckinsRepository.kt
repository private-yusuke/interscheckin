package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.models.Checkin
import javax.inject.Inject

class FakeFoursquareCheckinsRepository @Inject constructor() : FoursquareCheckinsRepository {
    private val exampleVenue = Checkin.V2Venue(
        id = "venue_id",
        name = "good venue",
        location = Checkin.V2Venue.Location(
            state = "茨城県",
            city = "つくば市",
            lat = 36.107565,
            lng = 140.104733,
        ),
        categories = listOf(
            Checkin.V2Venue.Category(
                id = "category_id",
                name = "Intersection",
                pluralName = "Intersections",
                shortName = "Intersection",
                icon = Checkin.V2Venue.Category.Icon(
                    prefix = "https://example.com/",
                    suffix = "foo.png",
                ),
                primary = true,
            ),
        ),
    )

    private val exampleCheckin = Checkin(
        id = "checkin_id",
        shout = "hey",
        createdAt = 100,
        type = "checkin",
        timeZoneOffset = 0,
        editableUntil = null,
        isMayor = false,
        score = Checkin.Score(
            total = 1,
        ),
        venue = exampleVenue,
    )

    override suspend fun createCheckin(
        venueId: String,
        shout: String,
        latitude: Double,
        longitude: Double,
    ): Checkin = exampleCheckin.copy(
        shout = shout,
        venue = exampleCheckin.venue.copy(
            id = venueId,
        ),
    )

    override suspend fun getCheckins(
        userId: Long?,
        offset: Long?,
        beforeTimestamp: Long?,
        limit: Long?,
    ): List<Checkin> =
        MutableList(100) { n ->
            exampleCheckin.copy(
                venue = exampleVenue.copy(
                    name = "test venue $n",
                ),
            )
        }
}
