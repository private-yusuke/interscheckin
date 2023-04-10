package pub.yusuke.interscheckin.ui.locationsettings

import kotlinx.coroutines.flow.StateFlow
import pub.yusuke.interscheckin.navigation.entity.PeriodicLocationRetrievalIntervalPreset

interface LocationSettingsContract {
    interface ViewModel {
        val screenStateFlow: StateFlow<ScreenState>
        fun setPeriodicLocationRetrievalEnabled(enabled: Boolean)
        fun setPeriodicLocationRetrievalPreset(preset: PeriodicLocationRetrievalIntervalPreset)
    }

    interface Interactor {
        suspend fun setPeriodicLocationRetrievalEnabled(enabled: Boolean)
        suspend fun getPeriodicLocationRetrievalEnabled(): Boolean
        suspend fun setPeriodicLocationRetrievalIntervalPreset(preset: PeriodicLocationRetrievalIntervalPreset)
        suspend fun getPeriodicLocationRetrievalIntervalPreset(): PeriodicLocationRetrievalIntervalPreset
    }

    sealed interface ScreenState {
        object Loading : ScreenState
        data class Idle(
            val periodicLocationRetrievalEnabled: Boolean,
            val periodicLocationRetrievalIntervalPreset: PeriodicLocationRetrievalIntervalPreset,
        ) : ScreenState
    }
}
