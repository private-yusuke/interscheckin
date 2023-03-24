package pub.yusuke.interscheckin.ui.splash

import kotlinx.coroutines.flow.StateFlow
import pub.yusuke.interscheckin.navigation.InterscheckinScreens

interface SplashContract {
    interface ViewModel {
        val nextInterscheckinScreenStateFlow: StateFlow<InterscheckinScreens?>

        fun onNavigatedToAnotherScreen()
    }

    interface Interactor {
        fun locationAccessAcquirementScreenAlreadyNavigatedTo(): Boolean
    }
}
