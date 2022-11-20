package pub.yusuke.interscheckin.ui.settings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface SettingsViewModelModule {
    @Binds
    fun bindInteractor(interactor: SettingsInteractor): SettingsContract.Interactor
}
