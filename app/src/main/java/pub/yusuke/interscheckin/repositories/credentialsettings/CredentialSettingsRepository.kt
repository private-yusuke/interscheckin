package pub.yusuke.interscheckin.repositories.credentialsettings

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class CredentialSettingsPreferences(
    val foursquareOAuthToken: String = "",
    val foursquareApiKey: String = "",
)

interface CredentialSettingsRepository {
    val credentialSettingsFlow: Flow<CredentialSettingsPreferences>

    suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String)
    suspend fun setFoursquareApiKey(foursquareApiKey: String)
    suspend fun fetchCredentialSettings(): CredentialSettingsPreferences
}
