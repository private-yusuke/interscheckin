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
        broadcast: String?,
        with: String?,
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

    override suspend fun fetchFriends(): List<pub.yusuke.foursquareclient.models.Friend> =
        listOf(
            pub.yusuke.foursquareclient.models.Friend(
                id = "friend1",
                firstName = "太郎",
                lastName = "田中",
                bio = "こんにちは！",
                photo = pub.yusuke.foursquareclient.models.Friend.Photo(
                    prefix = "https://example.com/photo_",
                    suffix = ".jpg",
                ),
                homeCity = "東京, 日本",
            ),
            pub.yusuke.foursquareclient.models.Friend(
                id = "friend2",
                firstName = "花子",
                lastName = "佐藤",
                bio = null,
                photo = null,
                homeCity = "大阪, 日本",
            ),
        )
}
