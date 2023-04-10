package pub.yusuke.interscheckin.repositories.periodiclocationretrieval

interface PeriodicLocationRetrievalRepository {
    var periodicLocationRetrievalEnabled: Boolean
    var periodicLocationRetrievalIntervalPreset: PeriodicLocationRetrievalIntervalPreset
}
