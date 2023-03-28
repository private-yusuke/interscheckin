package pub.yusuke.interscheckin.repositories.credentialsettings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FakeCredentialSettingsRepository @Inject constructor() : CredentialSettingsRepository {
    private val _settingsFlow = MutableStateFlow(
        CredentialSettingsPreferences(
            foursquareOAuthToken = "fake_token",
            foursquareApiKey = "fake_key",
        ),
    )

    override val credentialSettingsFlow: Flow<CredentialSettingsPreferences>
        get() = _settingsFlow

    override suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String) {
        val pref = _settingsFlow.first()
        _settingsFlow.tryEmit(pref.copy(foursquareOAuthToken = foursquareOAuthToken))
    }

    override suspend fun setFoursquareApiKey(foursquareApiKey: String) {
        val pref = _settingsFlow.first()
        _settingsFlow.tryEmit(pref.copy(foursquareApiKey = foursquareApiKey))
    }

    override suspend fun fetchCredentialSettings(): CredentialSettingsPreferences =
        _settingsFlow.first()
}
