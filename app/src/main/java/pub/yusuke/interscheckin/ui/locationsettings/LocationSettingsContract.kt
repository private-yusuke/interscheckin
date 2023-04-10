package pub.yusuke.interscheckin.ui.locationsettings

import kotlinx.coroutines.flow.StateFlow

interface LocationSettingsContract {
    interface ViewModel {
        val screenStateFlow: StateFlow<ScreenState>
        fun setPeriodicLocationRetrievalEnabled(enabled: Boolean)
        fun setPeriodicLocationRetrievalPreset(preset: PeriodicLocationRetrievalIntervalPreset)
    }

    interface Interactor {
        fun setPeriodicLocationRetrievalEnabled(enabled: Boolean)
        fun getPeriodicLocationRetrievalEnabled(): Boolean
        fun setPeriodicLocationRetrievalIntervalPreset(preset: PeriodicLocationRetrievalIntervalPreset)
        fun getPeriodicLocationRetrievalIntervalPreset(): PeriodicLocationRetrievalIntervalPreset
    }

    sealed interface PeriodicLocationRetrievalIntervalPreset {
        val interval: Long

        object High : PeriodicLocationRetrievalIntervalPreset {
            override val interval: Long
                get() = 1
        }

        object Medium : PeriodicLocationRetrievalIntervalPreset {
            override val interval: Long
                get() = 3
        }

        object Low : PeriodicLocationRetrievalIntervalPreset {
            override val interval: Long
                get() = 10
        }
    }

    sealed interface ScreenState {
        object Loading : ScreenState
        data class Idle(
            val periodicLocationRetrievalEnabled: Boolean,
            val periodicLocationRetrievalIntervalPreset: PeriodicLocationRetrievalIntervalPreset,
        ) : ScreenState
    }
}
