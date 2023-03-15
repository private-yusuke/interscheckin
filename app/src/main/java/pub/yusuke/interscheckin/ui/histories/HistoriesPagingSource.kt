package pub.yusuke.interscheckin.ui.histories

import androidx.paging.PagingSource
import androidx.paging.PagingState

class HistoriesPagingSource(
    private val userId: Long?,
    private val paging: HistoriesContract.Paging,
) : PagingSource<Long, HistoriesContract.Checkin>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, HistoriesContract.Checkin> {
        val offset = params.key ?: 0

        return runCatching {
            paging.load(
                userId = userId,
                offset = offset,
                perPage = params.loadSize.toLong(),
            )
        }.fold(
            onSuccess = {
                val pageSize = params.loadSize
                LoadResult.Page(
                    data = it,
                    prevKey = if (offset - pageSize > 0) offset - pageSize else null,
                    nextKey = if (it.isNotEmpty()) offset + pageSize else null,
                )
            },
            onFailure = {
                LoadResult.Error(it)
            },
        )
    }

    override fun getRefreshKey(state: PagingState<Long, HistoriesContract.Checkin>): Long? =
        state.anchorPosition?.let {
            val pageSize = state.config.pageSize
            val anchorOffset = state.closestPageToPosition(it)
            anchorOffset?.prevKey?.plus(pageSize) ?: anchorOffset?.nextKey?.minus(pageSize)
        }
}
