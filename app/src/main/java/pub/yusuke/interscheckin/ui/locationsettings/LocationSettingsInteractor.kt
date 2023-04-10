package pub.yusuke.interscheckin.ui.locationsettings

import pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset
import pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalRepository
import javax.inject.Inject

class LocationSettingsInteractor @Inject constructor(
    private val periodicLocationRetrievalRepository: PeriodicLocationRetrievalRepository,
) : LocationSettingsContract.Interactor {
    override fun setPeriodicLocationRetrievalEnabled(enabled: Boolean) {
        periodicLocationRetrievalRepository.periodicLocationRetrievalEnabled = enabled
    }

    override fun getPeriodicLocationRetrievalEnabled(): Boolean =
        periodicLocationRetrievalRepository.periodicLocationRetrievalEnabled

    override fun setPeriodicLocationRetrievalIntervalPreset(preset: LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset) {
        periodicLocationRetrievalRepository.periodicLocationRetrievalIntervalPreset = preset.translate()
    }

    override fun getPeriodicLocationRetrievalIntervalPreset(): LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset =
        periodicLocationRetrievalRepository.periodicLocationRetrievalIntervalPreset.translate()

    private fun LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset.translate(): PeriodicLocationRetrievalIntervalPreset = when (this) {
        LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset.High -> PeriodicLocationRetrievalIntervalPreset.High
        LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset.Medium -> PeriodicLocationRetrievalIntervalPreset.Medium
        LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset.Low -> PeriodicLocationRetrievalIntervalPreset.Low
    }

    private fun PeriodicLocationRetrievalIntervalPreset.translate(): LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset = when (this) {
        PeriodicLocationRetrievalIntervalPreset.High -> LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset.High
        PeriodicLocationRetrievalIntervalPreset.Medium -> LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset.Medium
        PeriodicLocationRetrievalIntervalPreset.Low -> LocationSettingsContract.PeriodicLocationRetrievalIntervalPreset.Low
    }
}
