package pub.yusuke.interscheckin.ui.main

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MainViewModelModule::class]
)
interface FakeMainViewModelModule {
    @Binds
    fun bindInteractor(interactor: MainInteractor): MainContract.Interactor
}
