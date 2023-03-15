package pub.yusuke.interscheckin.repositories.settings

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject

/**
 * 保存する内容を暗号化した上で保持します。
 */
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SettingsPreferences>,
) : SettingsRepository {
    override val settingsFlow: Flow<SettingsPreferences> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    emit(SettingsPreferences())
                } else {
                    throw it
                }
            }

    override suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String) {
        dataStore.updateData { preferences ->
            preferences.copy(foursquareOAuthToken = foursquareOAuthToken)
        }
    }

    override suspend fun setFoursquareApiKey(foursquareApiKey: String) {
        dataStore.updateData { preferences ->
            preferences.copy(foursquareApiKey = foursquareApiKey)
        }
    }

    override suspend fun fetchSettings(): SettingsPreferences =
        dataStore.data.first()
}
