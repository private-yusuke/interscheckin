package pub.yusuke.interscheckin.ui.locationaccessacquirement

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface LocationAccessAcquirementModule {
    @Binds
    fun bindInteractor(interactor: LocationAccessAcquirementInteractor): LocationAccessAcquirementContract.Interactor
}
