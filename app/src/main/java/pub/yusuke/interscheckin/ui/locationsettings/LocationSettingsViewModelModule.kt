package pub.yusuke.interscheckin.ui.locationsettings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface LocationSettingsViewModelModule {
    @Binds
    fun bindInteractor(interactor: LocationSettingsInteractor): LocationSettingsContract.Interactor
}
