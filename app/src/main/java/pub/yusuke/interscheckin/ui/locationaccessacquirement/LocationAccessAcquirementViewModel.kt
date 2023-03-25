package pub.yusuke.interscheckin.ui.locationaccessacquirement

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationAccessAcquirementViewModel @Inject constructor(
    private val interactor: LocationAccessAcquirementContract.Interactor,
) : ViewModel(), LocationAccessAcquirementContract.ViewModel {
    override fun onScreenRendered() {
        interactor.setLocationAccessAcquirementScreenDisplayed()
    }
}
