package pub.yusuke.interscheckin.repositories.visitedvenues

import co.anbora.labs.spatia.geometry.Point
import javax.inject.Inject

class FakeVisitedVenueDao @Inject constructor() : VisitedVenueDao {
    override suspend fun findByLatLong(
        latitude: Double,
        longitude: Double,
        limit: Long
    ): List<VisitedVenue> = listOf(
        VisitedVenue(
            id = "venue_id",
            name = "An visited Venue",
            categoriesString = "Intersection",
            location = Point(0.0, 0.0),
            iconName = "Intersection",
            iconUrl = "https://example.com/visited_venue.png"
        )
    )

    override suspend fun insertVisitedVenues(visitedVenues: List<VisitedVenue>) {}

    override suspend fun deleteAll() {}
}
