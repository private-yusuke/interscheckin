package pub.yusuke.interscheckin.navigation.entity

sealed interface PeriodicLocationRetrievalIntervalPreset {
    val interval: Long

    object High : PeriodicLocationRetrievalIntervalPreset {
        override val interval: Long
            get() = 10
    }

    object Medium : PeriodicLocationRetrievalIntervalPreset {
        override val interval: Long
            get() = 15
    }

    object Low : PeriodicLocationRetrievalIntervalPreset {
        override val interval: Long
            get() = 30
    }
}

fun PeriodicLocationRetrievalIntervalPreset.translate(): pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset = when (this) {
    PeriodicLocationRetrievalIntervalPreset.High -> pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset.High
    PeriodicLocationRetrievalIntervalPreset.Medium -> pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset.Medium
    PeriodicLocationRetrievalIntervalPreset.Low -> pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset.Low
}

fun pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset.translate(): PeriodicLocationRetrievalIntervalPreset = when (this) {
    pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset.High -> PeriodicLocationRetrievalIntervalPreset.High
    pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset.Medium -> PeriodicLocationRetrievalIntervalPreset.Medium
    pub.yusuke.interscheckin.repositories.periodiclocationretrieval.PeriodicLocationRetrievalIntervalPreset.Low -> PeriodicLocationRetrievalIntervalPreset.Low
}
