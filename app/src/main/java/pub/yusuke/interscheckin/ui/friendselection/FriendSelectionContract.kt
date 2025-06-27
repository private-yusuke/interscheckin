package pub.yusuke.interscheckin.ui.friendselection

import androidx.compose.runtime.State
import kotlinx.collections.immutable.ImmutableList
import pub.yusuke.interscheckin.navigation.entity.Friend

interface FriendSelectionContract {
    interface ViewModel {
        val friendsState: State<FriendsState>
        val selectedFriends: State<ImmutableList<Friend>>

        suspend fun loadFriends()
        fun toggleFriendSelection(friend: Friend)
        fun setInitialSelectedFriends(friends: List<Friend>)
    }

    interface Interactor {
        suspend fun fetchFriends(): List<Friend>
    }

    sealed class FriendsState {
        object Loading : FriendsState()
        data class Loaded(val friends: ImmutableList<Friend>) : FriendsState()
        data class Error(val throwable: Throwable) : FriendsState()
    }
}

