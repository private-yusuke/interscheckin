package pub.yusuke.interscheckin.repositories.userpreferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pub.yusuke.interscheckin.repositories.UserPreferences
import pub.yusuke.interscheckin.repositories.UserPreferencesRepository
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val LOG_TAG: String = "UserPreferencesRepositoryImpl"

private object PreferencesKeys {
    val DRIVING_MODE = booleanPreferencesKey("driving_mode")
}

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    constructor(context: Context) : this(context.dataStore)

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(LOG_TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { it.toUserPreferences() }

    override suspend fun enableDrivingMode(enable: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DRIVING_MODE] = enable
        }
    }

    override suspend fun fetchInitialPreferences(): UserPreferences =
        dataStore.data.first().toPreferences().toUserPreferences()

    private fun Preferences.toUserPreferences(): UserPreferences {
        val drivingMode = get(PreferencesKeys.DRIVING_MODE) ?: false
        return UserPreferences(drivingMode)
    }
}

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)
