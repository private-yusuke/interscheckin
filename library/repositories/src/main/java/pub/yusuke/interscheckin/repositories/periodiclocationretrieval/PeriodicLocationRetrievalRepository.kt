package pub.yusuke.interscheckin.repositories.periodiclocationretrieval

import kotlinx.coroutines.flow.Flow

data class PeriodicLocationRetrievalPreferences(
    val enabled: Boolean,
    val intervalPreset: PeriodicLocationRetrievalIntervalPreset,
)

interface PeriodicLocationRetrievalRepository {
    val periodicLocationRetrievalPreferencesFlow: Flow<PeriodicLocationRetrievalPreferences>

    suspend fun enablePeriodicLocationRetrieval(enabled: Boolean)
    suspend fun setIntervalPreset(preset: PeriodicLocationRetrievalIntervalPreset)
    suspend fun fetchInitialPreferences(): PeriodicLocationRetrievalPreferences
}
