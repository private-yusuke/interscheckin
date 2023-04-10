package pub.yusuke.interscheckin.repositories.periodiclocationretrieval

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException

private object PreferencesKeys {
    val ENABLED = booleanPreferencesKey("enabled")
    val INTERVAL_PRESET = stringPreferencesKey("interval_preset")
}

class PreferencesDataStorePeriodicLocationRetrievalRepository constructor(
    private val dataStore: DataStore<Preferences>,
) : PeriodicLocationRetrievalRepository {
    constructor(context: Context) : this(context.dataStore)

    override val periodicLocationRetrievalPreferencesFlow: Flow<PeriodicLocationRetrievalPreferences> =
        dataStore
            .data
            .catch {
                if (it is IOException) {
                    Log.e(LOG_TAG, "Error reading preferences", it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map { it.toPeriodicLocationRetrievalPreferences() }

    override suspend fun enablePeriodicLocationRetrieval(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLED] = enabled
        }
    }

    override suspend fun setIntervalPreset(preset: PeriodicLocationRetrievalIntervalPreset) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.INTERVAL_PRESET] = preset.name
        }
    }

    override suspend fun fetchInitialPreferences(): PeriodicLocationRetrievalPreferences =
        dataStore.data.first().toPreferences().toPeriodicLocationRetrievalPreferences()

    private fun Preferences.toPeriodicLocationRetrievalPreferences() = PeriodicLocationRetrievalPreferences(
        enabled = get(PreferencesKeys.ENABLED) ?: false,
        intervalPreset = get(PreferencesKeys.INTERVAL_PRESET)?.let { PeriodicLocationRetrievalIntervalPreset.valueOf(it) }
            ?: PeriodicLocationRetrievalIntervalPreset.Medium,
    )
}

private const val PERIODIC_LOCATION_RETRIEVAL_PREFERENCES_NAME = "periodic_location_retrieval_preferences"
private const val LOG_TAG: String = "UserPreferencesRepositoryImpl"
private val Context.dataStore by preferencesDataStore(
    name = PERIODIC_LOCATION_RETRIEVAL_PREFERENCES_NAME,
)
