package pub.yusuke.interscheckin.ui.histories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface HistoriesContract {
    interface ViewModel {
        val checkinsFlow: Flow<PagingData<Checkin>>
        val userId: Long?
    }

    interface Paging {
        suspend fun load(
            userId: Long? = null,
            beforeTimestamp: Long? = null,
            offset: Long? = null,
            perPage: Long,
        ): List<Checkin>
    }

    interface Interactor

    data class Checkin(
        val id: String,
        val shout: String?,
        val createdAt: Long,
        val venue: Venue,
        val score: Long?,
    ) {
        data class Venue(
            val id: String,
            val categoriesString: String,
            val name: String,
            val address: String?,
            val icon: Icon?,
        ) {
            data class Icon(
                val name: String,
                val url: String,
            )
        }
    }
}
