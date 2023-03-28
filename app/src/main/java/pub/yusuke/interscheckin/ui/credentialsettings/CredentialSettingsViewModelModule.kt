package pub.yusuke.interscheckin.ui.credentialsettings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface CredentialSettingsViewModelModule {
    @Binds
    fun bindInteractor(interactor: CredentialSettingsInteractor): CredentialSettingsContract.Interactor
}
