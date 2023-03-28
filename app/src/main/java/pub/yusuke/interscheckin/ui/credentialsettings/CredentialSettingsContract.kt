package pub.yusuke.interscheckin.ui.credentialsettings

import kotlinx.coroutines.flow.Flow

interface CredentialSettingsContract {
    interface ViewModel {
        val foursquareOAuthTokenFlow: Flow<String>
        val foursquareApiKeyFlow: Flow<String>

        suspend fun saveSettings(
            foursquareOAuthToken: String,
            foursquareApiKey: String,
        )
    }

    interface Interactor {
        fun fetchFoursquareOAuthTokenFlow(): Flow<String>
        fun fetchFoursquareApiKeyFlow(): Flow<String>

        suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String)
        suspend fun setFoursquareApiKey(foursquareApiKey: String)
    }
}
