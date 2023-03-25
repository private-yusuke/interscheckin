package pub.yusuke.interscheckin.ui.splash

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface SplashModule {
    @Binds
    fun bindInteractor(interactor: SplashInteractor): SplashContract.Interactor
}
