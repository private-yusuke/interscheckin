package pub.yusuke.interscheckin.ui.friendselection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface FriendSelectionViewModelModule {
    @Binds
    fun bindInteractor(interactor: FriendSelectionInteractor): FriendSelectionContract.Interactor
}

