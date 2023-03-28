package pub.yusuke.interscheckin.ui.credentialsettings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pub.yusuke.interscheckin.repositories.credentialsettings.CredentialSettingsRepository
import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenueDao
import javax.inject.Inject

class CredentialSettingsInteractor @Inject constructor(
    private val credentialSettingsRepository: CredentialSettingsRepository,
    private val visitedVenueDao: VisitedVenueDao,
) : CredentialSettingsContract.Interactor {
    override fun fetchFoursquareOAuthTokenFlow(): Flow<String> =
        credentialSettingsRepository
            .credentialSettingsFlow
            .map { it.foursquareOAuthToken }

    override fun fetchFoursquareApiKeyFlow(): Flow<String> =
        credentialSettingsRepository
            .credentialSettingsFlow
            .map { it.foursquareApiKey }

    override suspend fun setFoursquareOAuthToken(foursquareOAuthToken: String) =
        credentialSettingsRepository.setFoursquareOAuthToken(foursquareOAuthToken)

    override suspend fun setFoursquareApiKey(foursquareApiKey: String) =
        credentialSettingsRepository.setFoursquareApiKey(foursquareApiKey)

    override suspend fun resetCachedVenuesDatabase() =
        visitedVenueDao.deleteAll()
}
