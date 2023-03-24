package pub.yusuke.interscheckin.ui.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pub.yusuke.interscheckin.navigation.InterscheckinScreens
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val interactor: SplashInteractor,
) : ViewModel(), SplashContract.ViewModel {
    private val _nextInterscheckinScreenStateFlow: MutableStateFlow<InterscheckinScreens?> =
        MutableStateFlow(null)
    override val nextInterscheckinScreenStateFlow: StateFlow<InterscheckinScreens?> =
        _nextInterscheckinScreenStateFlow

    override fun onNavigatedToAnotherScreen() {
        _nextInterscheckinScreenStateFlow.value = null
    }

    init {
        fetchContents()
    }

    private fun fetchContents() {
        _nextInterscheckinScreenStateFlow.value =
            if (interactor.locationAccessAcquirementScreenAlreadyNavigatedTo()) {
                InterscheckinScreens.Main
            } else {
                InterscheckinScreens.LocationAccessAcquirement
            }
    }
}
