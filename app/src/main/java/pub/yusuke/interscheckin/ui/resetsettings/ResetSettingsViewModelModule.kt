package pub.yusuke.interscheckin.ui.resetsettings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface ResetSettingsViewModelModule {
    @Binds
    fun bindInteractor(interactor: ResetSettingsInteractor): ResetSettingsContract.Interactor
}
