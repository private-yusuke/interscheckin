package pub.yusuke.interscheckin.ui.locationsettings

import pub.yusuke.interscheckin.navigation.entity.PeriodicLocationRetrievalIntervalPreset
import pub.yusuke.interscheckin.navigation.entity.translate
import pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalRepository
import javax.inject.Inject

class LocationSettingsInteractor @Inject constructor(
    private val periodicLocationRetrievalRepository: PeriodicLocationRetrievalRepository,
) : LocationSettingsContract.Interactor {
    override suspend fun setPeriodicLocationRetrievalEnabled(enabled: Boolean) {
        periodicLocationRetrievalRepository.enablePeriodicLocationRetrieval(enabled)
    }

    override suspend fun getPeriodicLocationRetrievalEnabled(): Boolean =
        periodicLocationRetrievalRepository.fetchInitialPreferences().enabled

    override suspend fun setPeriodicLocationRetrievalIntervalPreset(preset: PeriodicLocationRetrievalIntervalPreset) {
        periodicLocationRetrievalRepository.setIntervalPreset(preset.translate())
    }

    override suspend fun getPeriodicLocationRetrievalIntervalPreset(): PeriodicLocationRetrievalIntervalPreset =
        periodicLocationRetrievalRepository.fetchInitialPreferences().intervalPreset.translate()
}
