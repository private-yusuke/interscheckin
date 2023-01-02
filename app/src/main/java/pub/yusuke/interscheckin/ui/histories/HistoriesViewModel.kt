package pub.yusuke.interscheckin.ui.histories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HistoriesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    paging: HistoriesContract.Paging
) : ViewModel(), HistoriesContract.ViewModel {
    override val userId: Long? = savedStateHandle["userId"]

    override val checkinsFlow: Flow<PagingData<HistoriesContract.Checkin>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            initialKey = null,
            pagingSourceFactory = {
                HistoriesPagingSource(userId, paging)
            }
        ).flow.cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 50
        private const val INITIAL_LOAD_SIZE = 100
    }
}
