package pub.yusuke.interscheckin.ui.locationaccessacquirement

interface LocationAccessAcquirementContract {
    interface ViewModel {
        fun onScreenRendered()
    }

    interface Interactor {
        fun setLocationAccessAcquirementScreenDisplayed()
    }

    sealed interface ScreenState {
        /**
         * この画面に遷移してきてから位置情報へのアクセスの可能性などを調査しており表示すべき画面が未だ定まらないとき
         */
        object Loading : ScreenState

        /**
         * 位置情報へのアクセスが一切不可能だがダイアログを表示できるとき（初回起動時は基本的にこれになる）
         */
        object NoLocationAccessAcquired : ScreenState

        /**
         * 大まかな位置情報へのアクセスのみができ、かつダイアログを表示できるとき
         */
        object OnlyCoarseLocationAccessAcquired : ScreenState

        /**
         * 詳細な位置情報へのアクセスができるとき
         */
        object PreciseLocationAccessAcquired : ScreenState

        /**
         * 大まかな位置情報へのアクセスのみができ、かつダイアログを表示できないとき
         */
        object PreciseLocationAccessNotAcquirable : ScreenState

        /**
         * 位置情報へのアクセスが一切不可能かつダイアログを表示できないとき
         */
        object LocationAccessNotAcquirable : ScreenState
    }
}
