package pub.yusuke.interscheckin.repositories.periodiclocationretrieval

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesPeriodicLocationRetrievalRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : PeriodicLocationRetrievalRepository {
    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    override var periodicLocationRetrievalEnabled: Boolean
        get() = preferences.getBoolean(KEY_PERIODIC_LOCATION_RETRIEVAL_ENABLED, false)
        set(value) {
            preferences.edit {
                putBoolean(KEY_PERIODIC_LOCATION_RETRIEVAL_ENABLED, value)
            }
        }
    override var periodicLocationRetrievalIntervalPreset: PeriodicLocationRetrievalIntervalPreset
        get() = PeriodicLocationRetrievalIntervalPreset.valueOf(
            requireNotNull(
                preferences.getString(
                    KEY_PERIODIC_LOCATION_RETRIEVAL_INTERVAL_PRESET,
                    PeriodicLocationRetrievalIntervalPreset.Medium.name,
                ),
            ),
        )
        set(value) {
            preferences.edit {
                putString(KEY_PERIODIC_LOCATION_RETRIEVAL_INTERVAL_PRESET, value.name)
            }
        }

    companion object {
        const val FILE_NAME = "periodic_location_retrieval"
        private const val KEY_PERIODIC_LOCATION_RETRIEVAL_ENABLED =
            "periodicLocationRetrievalEnabled"
        private const val KEY_PERIODIC_LOCATION_RETRIEVAL_INTERVAL_PRESET =
            "periodicLocationRetrievalIntervalPreset"
    }
}
