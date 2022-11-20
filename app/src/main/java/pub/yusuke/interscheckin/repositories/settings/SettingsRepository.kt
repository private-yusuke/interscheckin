package pub.yusuke.interscheckin.repositories.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class SettingsPreferences(
    val foursquareOAuthToken: String = "",
    val foursquareApiKey: String = ""
)

interface SettingsRepository {
    val settingsFlow: Flow<SettingsPreferences>

    suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String)
    suspend fun setFoursquareApiKey(foursquareApiKey: String)
    suspend fun fetchSettings(): SettingsPreferences
}
