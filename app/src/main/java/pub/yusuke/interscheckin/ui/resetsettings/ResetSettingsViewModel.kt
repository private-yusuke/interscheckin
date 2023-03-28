package pub.yusuke.interscheckin.ui.resetsettings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetSettingsViewModel @Inject constructor(
    private val interactor: ResetSettingsInteractor,
) : ViewModel(), ResetSettingsContract.ViewModel {
    override suspend fun resetCachedVenues() {
        interactor.resetCachedVenuesDatabase()
    }
}
