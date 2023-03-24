package pub.yusuke.interscheckin.ui.locationaccessacquirement

import pub.yusuke.interscheckin.repositories.locationaccessacquirementscreendisplayedonce.LocationAccessAcquirementScreenDisplayedOnceRepository
import javax.inject.Inject

class LocationAccessAcquirementInteractor @Inject constructor(
    private val locationAccessAcquirementScreenDisplayedOnceRepository: LocationAccessAcquirementScreenDisplayedOnceRepository,
) : LocationAccessAcquirementContract.Interactor {
    override fun setLocationAccessAcquirementScreenDisplayed() {
        locationAccessAcquirementScreenDisplayedOnceRepository
            .locationAccessAcquirementScreenDisplayedOnce = true
    }
}
