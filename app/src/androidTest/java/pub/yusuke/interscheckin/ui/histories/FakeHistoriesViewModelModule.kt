package pub.yusuke.interscheckin.ui.histories

import androidx.lifecycle.SavedStateHandle
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.spyk

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HistoriesViewModelModule::class]
)
interface FakeHistoriesViewModelModule {
    @Binds
    fun bindPaging(paging: HistoriesPaging): HistoriesContract.Paging

    companion object {
        @Provides
        fun provideViewModel(
            paging: HistoriesContract.Paging
        ): HistoriesContract.ViewModel = spyk(HistoriesViewModel(SavedStateHandle(), paging))
    }
}
