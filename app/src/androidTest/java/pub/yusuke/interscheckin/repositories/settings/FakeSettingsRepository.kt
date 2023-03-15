package pub.yusuke.interscheckin.repositories.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FakeSettingsRepository @Inject constructor() : SettingsRepository {
    private val _settingsFlow = MutableStateFlow(
        SettingsPreferences(
            foursquareOAuthToken = "fake_token",
            foursquareApiKey = "fake_key",
        ),
    )

    override val settingsFlow: Flow<SettingsPreferences>
        get() = _settingsFlow

    override suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String) {
        val pref = _settingsFlow.first()
        _settingsFlow.tryEmit(pref.copy(foursquareOAuthToken = foursquareOAuthToken))
    }

    override suspend fun setFoursquareApiKey(foursquareApiKey: String) {
        val pref = _settingsFlow.first()
        _settingsFlow.tryEmit(pref.copy(foursquareApiKey = foursquareApiKey))
    }

    override suspend fun fetchSettings(): SettingsPreferences =
        _settingsFlow.first()
}
