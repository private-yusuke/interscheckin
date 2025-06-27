package pub.yusuke.interscheckin.ui.friendselection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.navigation.entity.Friend
import pub.yusuke.interscheckin.navigation.entity.url
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme

@Composable
fun FriendSelectionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FriendSelectionContract.ViewModel = hiltViewModel<FriendSelectionViewModel>(),
) {
    val friendsState by viewModel.friendsState
    val selectedFriends by viewModel.selectedFriends

    InterscheckinTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                FriendSelectionTopBar(
                    onNavigateBack = { navController.popBackStack() },
                    onConfirm = {
                        // TODO: Pass selected friends back to previous screen
                        navController.popBackStack()
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when (friendsState) {
                    FriendSelectionContract.FriendsState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is FriendSelectionContract.FriendsState.Error -> {
                        ErrorContent(
                            reason = friendsState.throwable.message ?: "Unknown error",
                            onClick = { /* TODO: Retry loading */ },
                        )
                    }
                    is FriendSelectionContract.FriendsState.Loaded -> {
                        if (friendsState.friends.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "友達が見つかりませんでした",
                                    modifier = Modifier
                                        .semantics { contentDescription = "No friends found" },
                                )
                            }
                        } else {
                            FriendSelectionContent(
                                friends = friendsState.friends,
                                selectedFriends = selectedFriends,
                                onFriendToggle = viewModel::toggleFriendSelection,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendSelectionContent(
    friends: kotlinx.collections.immutable.ImmutableList<Friend>,
    selectedFriends: kotlinx.collections.immutable.ImmutableList<Friend>,
    onFriendToggle: (Friend) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 16.dp,
            start = 12.dp,
            end = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(friends) { friend ->
            FriendRow(
                friend = friend,
                isSelected = selectedFriends.contains(friend),
                onToggle = { onFriendToggle(friend) },
            )
        }
    }
}

@Composable
private fun FriendRow(
    friend: Friend,
    isSelected: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(8.dp),
    ) {
        if (friend.photo == null) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(percent = 100))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = friend.firstName.take(1),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(percent = 100))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(friend.photo.url()),
                    contentDescription = "${friend.displayName}のプロフィール写真",
                    modifier = Modifier.size(48.dp),
                )
            }
        }
        
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = friend.displayName,
                style = MaterialTheme.typography.titleMedium,
            )
            friend.homeCity?.let { homeCity ->
                Text(
                    text = homeCity,
                    style = TextStyle.Default.copy(color = Color.Gray),
                )
            }
        }
        
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
        )
    }
}

@Composable
private fun ErrorContent(
    reason: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("友達の読み込み中にエラーが発生しました")
        Text(reason)
        Button(onClick = onClick) {
            Text("再試行")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FriendSelectionTopBar(
    onNavigateBack: () -> Unit,
    onConfirm: () -> Unit,
) {
    TopAppBar(
        title = { Text("友達を選択") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Filled.ArrowBack, "戻る")
            }
        },
        actions = {
            IconButton(onClick = onConfirm) {
                Icon(Icons.Filled.Check, "完了")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriendRow() {
    InterscheckinTheme {
        FriendRow(
            friend = exampleFriend,
            isSelected = true,
            onToggle = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriendSelectionContent() {
    InterscheckinTheme {
        FriendSelectionContent(
            friends = listOf(exampleFriend, exampleFriend2).toImmutableList(),
            selectedFriends = persistentListOf(exampleFriend),
            onFriendToggle = { },
        )
    }
}

private val exampleFriend = Friend(
    id = "friend1",
    firstName = "太郎",
    lastName = "田中",
    bio = "こんにちは！",
    photo = Friend.Photo(
        prefix = "https://example.com/photo_",
        suffix = ".jpg",
    ),
    homeCity = "東京, 日本",
)

private val exampleFriend2 = Friend(
    id = "friend2",
    firstName = "花子",
    lastName = "佐藤",
    bio = null,
    photo = null,
    homeCity = "大阪, 日本",
)