package pub.yusuke.interscheckin.ui.histories.util

import pub.yusuke.interscheckin.ui.histories.HistoriesContract

object HistoriesScreenTestData {
    val venue =
        HistoriesContract.Checkin.Venue(
            id = "abc",
            name = "test venue",
            categoriesString = "test categories",
            address = "Ibaraki, Tsukuba-shi",
            icon = HistoriesContract.Checkin.Venue.Icon(
                name = "icon name",
                url = "https://example.com/test.icon"
            )
        )

    val checkin = HistoriesContract.Checkin(
        id = "def",
        shout = "最高！",
        createdAt = 1672841580,
        venue = venue,
        score = 200
    )

    val checkins: List<HistoriesContract.Checkin> = MutableList(10) { n ->
        checkin.copy(
            venue = venue.copy(
                name = "test venue $n"
            )
        )
    }
}
