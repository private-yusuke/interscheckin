package pub.yusuke.interscheckin.repositories.foursquarecheckins

import pub.yusuke.foursquareclient.models.Category
import pub.yusuke.foursquareclient.models.Geocodes
import pub.yusuke.foursquareclient.models.Icon
import pub.yusuke.foursquareclient.models.LatAndLong
import pub.yusuke.foursquareclient.models.Location
import pub.yusuke.foursquareclient.models.Venue
import javax.inject.Inject

class FakeFoursquarePlacesRepository @Inject constructor() : FoursquarePlacesRepository {
    override suspend fun searchPlacesNearby(
        latitude: Double,
        longitude: Double,
        hacc: Double?,
        limit: Int?,
        query: String?,
    ): List<Venue> =
        listOf(
            Venue(
                categories = listOf(
                    Category(
                        id = "testCategoryId",
                        name = "testCategory",
                        icon = Icon("prefix", "suffix"),
                    ),
                ),
                chains = listOf(),
                distance = -1,
                fsqId = "fsq_id",
                geocodes = Geocodes(
                    LatAndLong(
                        latitude = 0.0,
                        longitude = 0.0,
                    ),
                ),
                link = "https://example.com/",
                location = Location(
                    address = "210 S King St",
                    addressExtended = null,
                    censusBlock = "511076105052013",
                    country = "US",
                    crossStreet = null,
                    dma = "Washington, Dc-Hagrstwn",
                    formattedAddress = "210 S King St, Leesburg, VA 20175",
                    locality = "Leesburg",
                    postcode = "20175",
                    region = "バージニア州",
                ),
                name = "Black Walnut Brewery",
                relatedPlaces = null,
                timezone = "America/New_York",
            ),
        )
}
