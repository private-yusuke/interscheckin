package pub.yusuke.interscheckin.ui.resetsettings

import pub.yusuke.interscheckin.repositories.visitedvenues.VisitedVenueDao
import javax.inject.Inject

class ResetSettingsInteractor @Inject constructor(
    private val visitedVenueDao: VisitedVenueDao,
) : ResetSettingsContract.Interactor {
    override suspend fun resetCachedVenuesDatabase() {
        visitedVenueDao.deleteAll()
    }
}
