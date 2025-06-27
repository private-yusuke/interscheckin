package pub.yusuke.interscheckin.ui.friendselection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import pub.yusuke.interscheckin.navigation.entity.Friend
import javax.inject.Inject

@HiltViewModel
class FriendSelectionViewModel @Inject constructor(
    private val interactor: FriendSelectionContract.Interactor,
) : ViewModel(), FriendSelectionContract.ViewModel {

    private val _friendsState: MutableState<FriendSelectionContract.FriendsState> =
        mutableStateOf(FriendSelectionContract.FriendsState.Loading)
    override val friendsState: State<FriendSelectionContract.FriendsState> = _friendsState

    private val _selectedFriends: MutableState<ImmutableList<Friend>> =
        mutableStateOf(persistentListOf())
    override val selectedFriends: State<ImmutableList<Friend>> = _selectedFriends

    init {
        viewModelScope.launch {
            loadFriends()
        }
    }

    override suspend fun loadFriends() {
        _friendsState.value = FriendSelectionContract.FriendsState.Loading
        
        runCatching {
            interactor.fetchFriends()
        }.onSuccess { friends ->
            _friendsState.value = FriendSelectionContract.FriendsState.Loaded(
                friends = friends.toImmutableList()
            )
        }.onFailure { throwable ->
            _friendsState.value = FriendSelectionContract.FriendsState.Error(throwable)
        }
    }

    override fun toggleFriendSelection(friend: Friend) {
        val currentSelection = _selectedFriends.value
        _selectedFriends.value = if (currentSelection.contains(friend)) {
            currentSelection.remove(friend)
        } else {
            currentSelection.add(friend)
        }
    }

    override fun setInitialSelectedFriends(friends: List<Friend>) {
        _selectedFriends.value = friends.toImmutableList()
    }
}