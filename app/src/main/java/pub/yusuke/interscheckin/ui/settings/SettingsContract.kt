package pub.yusuke.interscheckin.ui.settings

import kotlinx.coroutines.flow.Flow

interface SettingsContract {
    interface ViewModel {
        val reasonId: Int?
        val foursquareOAuthTokenFlow: Flow<String>
        val foursquareApiKeyFlow: Flow<String>

        suspend fun saveSettings(
            foursquareOAuthToken: String,
            foursquareApiKey: String,
        )

        suspend fun resetCachedVenues()
    }

    interface Interactor {
        fun fetchFoursquareOAuthTokenFlow(): Flow<String>
        fun fetchFoursquareApiKeyFlow(): Flow<String>

        suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String)
        suspend fun setFoursquareApiKey(foursquareApiKey: String)

        suspend fun resetCachedVenuesDatabase()
    }
}
