package pub.yusuke.interscheckin.ui.histories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface HistoriesViewModelModule {
    @Binds
    fun bindPaging(paging: HistoriesPaging): HistoriesContract.Paging
}
