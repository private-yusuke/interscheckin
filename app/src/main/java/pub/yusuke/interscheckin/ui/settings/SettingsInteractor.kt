package pub.yusuke.interscheckin.ui.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pub.yusuke.interscheckin.repositories.settings.SettingsRepository
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenueDao
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val visitedVenueDao: VisitedVenueDao,
) : SettingsContract.Interactor {
    override fun fetchFoursquareOAuthTokenFlow(): Flow<String> =
        settingsRepository
            .settingsFlow
            .map { it.foursquareOAuthToken }

    override fun fetchFoursquareApiKeyFlow(): Flow<String> =
        settingsRepository
            .settingsFlow
            .map { it.foursquareApiKey }

    override suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String) =
        settingsRepository.setFoursquareOAuthToken(foursquareOAuthToken)

    override suspend fun setFoursquareApiKey(foursquareApiKey: String) =
        settingsRepository.setFoursquareApiKey(foursquareApiKey)

    override suspend fun resetCachedVenuesDatabase() =
        visitedVenueDao.deleteAll()
}
