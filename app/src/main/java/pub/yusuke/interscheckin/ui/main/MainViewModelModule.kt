package pub.yusuke.interscheckin.ui.main

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface MainViewModelModule {
    @Binds
    fun bindInteractor(interactor: MainInteractor): MainContract.Interactor
}
