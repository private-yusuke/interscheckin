package pub.yusuke.interscheckin.ui.credentialsettings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CredentialSettingsViewModel @Inject constructor(
    private val interactor: CredentialSettingsContract.Interactor,
) : ViewModel(), CredentialSettingsContract.ViewModel {
    override val foursquareOAuthTokenFlow: Flow<String> =
        interactor.fetchFoursquareOAuthTokenFlow()
    override val foursquareApiKeyFlow: Flow<String> =
        interactor.fetchFoursquareApiKeyFlow()

    override suspend fun saveSettings(foursquareOAuthToken: String, foursquareApiKey: String) {
        interactor.setFoursquareOAuthToken(foursquareOAuthToken)
        interactor.setFoursquareApiKey(foursquareApiKey)
    }

    override suspend fun resetCachedVenues() = interactor.resetCachedVenuesDatabase()
}
