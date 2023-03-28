package pub.yusuke.interscheckin.ui.resetsettings

interface ResetSettingsContract {
    interface ViewModel {
        suspend fun resetCachedVenues()
    }

    interface Interactor {
        suspend fun resetCachedVenuesDatabase()
    }
}
