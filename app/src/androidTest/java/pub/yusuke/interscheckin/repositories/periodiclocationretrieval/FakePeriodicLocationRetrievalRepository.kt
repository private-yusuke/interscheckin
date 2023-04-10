package pub.yusuke.interscheckin.repositories.periodiclocationretrieval

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class FakePeriodicLocationRetrievalRepository @Inject constructor() : PeriodicLocationRetrievalRepository {
    private val _periodicLocationRetrievalPreferencesFlow = MutableStateFlow(
        PeriodicLocationRetrievalPreferences(
            enabled = false,
            intervalPreset = PeriodicLocationRetrievalIntervalPreset.Medium
        ),
    )
    override val periodicLocationRetrievalPreferencesFlow: Flow<PeriodicLocationRetrievalPreferences> =
        _periodicLocationRetrievalPreferencesFlow.asStateFlow()

    override suspend fun enablePeriodicLocationRetrieval(enabled: Boolean) {
        _periodicLocationRetrievalPreferencesFlow.value = _periodicLocationRetrievalPreferencesFlow.value.copy(enabled = enabled)
    }

    override suspend fun setIntervalPreset(preset: PeriodicLocationRetrievalIntervalPreset) {
        _periodicLocationRetrievalPreferencesFlow.value = _periodicLocationRetrievalPreferencesFlow.value.copy(intervalPreset = preset)
    }

    override suspend fun fetchInitialPreferences(): PeriodicLocationRetrievalPreferences =
        _periodicLocationRetrievalPreferencesFlow.value
}
