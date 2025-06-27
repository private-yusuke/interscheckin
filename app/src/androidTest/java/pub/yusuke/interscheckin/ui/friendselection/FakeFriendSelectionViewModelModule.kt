package pub.yusuke.interscheckin.ui.friendselection

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.spyk
import pub.yusuke.foursquareclient.FoursquareClient

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FriendSelectionViewModelModule::class],
)
interface FakeFriendSelectionViewModelModule {
    @Binds
    fun bindInteractor(interactor: FriendSelectionInteractor): FriendSelectionContract.Interactor

    companion object {
        @Provides
        fun provideViewModel(
            interactor: FriendSelectionContract.Interactor,
        ): FriendSelectionContract.ViewModel = spyk(FriendSelectionViewModel(interactor))

        @Provides
        fun provideInteractor(
            foursquareClient: FoursquareClient,
        ): FriendSelectionInteractor = FriendSelectionInteractor(foursquareClient)
    }
}