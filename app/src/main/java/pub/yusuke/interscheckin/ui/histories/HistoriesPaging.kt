package pub.yusuke.interscheckin.ui.histories

import co.anbora.labs.spatia.geometry.Point
import pub.yusuke.foursquareclient.models.Checkin
import pub.yusuke.foursquareclient.models.url
import pub.yusuke.interscheckin.repositories.foursquarecheckins.FoursquareCheckinsRepository
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenue
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenueDao
import javax.inject.Inject

class HistoriesPaging @Inject constructor(
    private val foursquareCheckinsRepository: FoursquareCheckinsRepository,
    private val visitedVenueDao: VisitedVenueDao
) : HistoriesContract.Paging {
    override suspend fun load(
        userId: Long?,
        beforeTimestamp: Long?,
        offset: Long?,
        perPage: Long
    ): List<HistoriesContract.Checkin> {
        val checkins = foursquareCheckinsRepository.getCheckins(
            userId = userId,
            offset = offset,
            beforeTimestamp = beforeTimestamp,
            limit = perPage
        )
        visitedVenueDao.insertVisitedVenues(checkins.translateToVisitedVenues())

        return checkins.translateToCheckins()
    }

    private fun List<Checkin>.translateToCheckins(): List<HistoriesContract.Checkin> =
        this.map { it.translateToCheckin() }

    private fun Checkin.translateToCheckin(): HistoriesContract.Checkin =
        HistoriesContract.Checkin(
            id = this.id,
            shout = this.shout,
            createdAt = this.createdAt,
            venue = this.venue.translateToVenue(),
            score = this.score?.total
        )

    private fun Checkin.V2Venue.translateToVenue(): HistoriesContract.Checkin.Venue =
        HistoriesContract.Checkin.Venue(
            id = this.id,
            name = this.name,
            address = this.location.let {
                listOfNotNull(it.state, it.city).joinToString(" ")
            },
            categoriesString = this.categories.joinToString(", ") { it.name },
            icon = this.categories.firstOrNull()?.translateToIcon()
        )

    private fun Checkin.V2Venue.Category.translateToIcon(): HistoriesContract.Checkin.Venue.Icon =
        HistoriesContract.Checkin.Venue.Icon(
            name = this.name,
            url = this.icon.url()
        )

    private fun List<Checkin>.translateToVisitedVenues(): List<VisitedVenue> =
        map { it.translateToVisitedVenue() }

    private fun Checkin.translateToVisitedVenue(): VisitedVenue =
        VisitedVenue(
            id = this.venue.id,
            name = this.venue.name,
            categoriesString = this.venue.categories.joinToString(", ") { it.name },
            location = Point(
                this.venue.location.lat,
                this.venue.location.lng
            ),
            iconName = this.venue.categories.firstOrNull()?.name,
            iconUrl = this.venue.categories.firstOrNull()?.icon?.url()
        )
}
