package pub.yusuke.interscheckin.ui.main.util

import android.location.Location
import kotlinx.collections.immutable.toImmutableList
import pub.yusuke.interscheckin.ui.main.MainContract

object MainScreenTestData {
    val venue =
        MainContract.Venue(
            id = "abc",
            name = "test venue",
            categoriesString = "test categories",
            distance = 1,
            icon = MainContract.Venue.Icon(
                name = "icon name",
                url = "https://example.com/test.icon",
            ),
        )

    val venuesStateIdle = MainContract.VenuesState.Idle(listOf(venue).toImmutableList())
    val venuesStateLoading = MainContract.VenuesState.Loading(listOf(venue).toImmutableList())

    val location = Location("")

    val locationStateLoaded = MainContract.LocationState.Loaded(location)
    val locationStateLoading = MainContract.LocationState.Loading(location)
}
