package pub.yusuke.interscheckin.ui.splash

import pub.yusuke.interscheckin.repositories.locationaccessacquirementscreendisplayedonce.LocationAccessAcquirementScreenDisplayedOnceRepository
import javax.inject.Inject

class SplashInteractor @Inject constructor(
    private val locationAccessAcquirementScreenDisplayedOnceRepository: LocationAccessAcquirementScreenDisplayedOnceRepository,
) : SplashContract.Interactor {
    override fun locationAccessAcquirementScreenAlreadyNavigatedTo() =
        locationAccessAcquirementScreenDisplayedOnceRepository.locationAccessAcquirementScreenDisplayedOnce
}
