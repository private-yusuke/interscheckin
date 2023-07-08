package pub.yusuke.interscheckin.ui.histories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.flowOf
import pub.yusuke.interscheckin.R
import pub.yusuke.interscheckin.ui.theme.InterscheckinTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun HistoriesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HistoriesContract.ViewModel = hiltViewModel<HistoriesViewModel>(),
) {
    val lazyPagingItems = viewModel.checkinsFlow.collectAsLazyPagingItems()

    InterscheckinTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                HistoriesTopBar(
                    onNavigateToPreviousRoute = { navController.popBackStack() },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                lazyPagingItems.apply {
                    when (val it = loadState.refresh) {
                        LoadState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is LoadState.Error -> {
                            ErrorContent(
                                reason = it.error.message ?: "unknown",
                                onClick = { lazyPagingItems.refresh() },
                            )
                        }
                        is LoadState.NotLoading -> {
                            if (lazyPagingItems.itemCount == 0) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = stringResource(R.string.histories_no_histories_found),
                                        modifier = Modifier
                                            .semantics { contentDescription = "No histories found" },
                                    )
                                }
                            } else {
                                HistoriesContent(
                                    lazyPagingItems = lazyPagingItems,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoriesContent(
    lazyPagingItems: LazyPagingItems<HistoriesContract.Checkin>,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 16.dp,
            start = 12.dp,
            end = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        items(lazyPagingItems) {
            it?.let { checkin ->
                CheckinRow(
                    checkin = checkin,
                )
            }
        }

        when (val it = lazyPagingItems.loadState.append) {
            LoadState.Loading -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> item {
                Text("An error occurred while loading checkins")
                it.error.message?.let {
                    Text(it)
                }
            }
            is LoadState.NotLoading -> {}
        }
    }
}

@Composable
private fun CheckinRow(
    checkin: HistoriesContract.Checkin,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (checkin.venue.icon == null) {
            Text("no icon")
        } else {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(percent = 100))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(checkin.venue.icon.url),
                    contentDescription = checkin.venue.icon.name,
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inversePrimary),
                )
            }
        }
        Column {
            Text(checkin.venue.name)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                checkin.venue.address?.let {
                    Text(
                        text = it,
                        style = TextStyle.Default.copy(color = Color.Gray),
                    )
                }
                checkin.score?.let {
                    Text(
                        text = "🪙 $it",
                        style = TextStyle.Default.copy(color = Color.Gray),
                    )
                }
            }
            Text(
                text = Instant
                    .ofEpochSecond(checkin.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                style = TextStyle.Default.copy(color = Color.Gray),
            )
        }
    }
}

@Composable
private fun ErrorContent(
    reason: String,
    onClick: () -> Unit,
) {
    Column {
        Text("An error occurred while loading check-ins")
        Text(reason)
        Button(onClick = onClick) {
            Text("Reload")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoriesTopBar(
    onNavigateToPreviousRoute: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(R.string.histories_topbar_title)) },
        navigationIcon = {
            IconButton(onClick = onNavigateToPreviousRoute) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
//        backgroundColor = MaterialTheme.colors.primary,
//        contentColor = Color.White,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewHistoriesContent() {
    HistoriesContent(
        lazyPagingItems = flowOf(PagingData.from(MutableList(5) { exampleCheckin })).collectAsLazyPagingItems(),
    )
}

@Preview
@Composable
private fun PreviewCheckinRow() {
    CheckinRow(checkin = exampleCheckin)
}

private val exampleCheckin = HistoriesContract.Checkin(
    id = "checkin_id",
    shout = "hello",
    createdAt = 1672556548,
    venue = HistoriesContract.Checkin.Venue(
        id = "venue_id",
        categoriesString = "Intersection",
        name = "ABC intersection",
        address = "Somewhere",
        icon = HistoriesContract.Checkin.Venue.Icon(
            name = "Intersection",
            url = "https://example.com/foo.png",
        ),
    ),
    score = 20,
)
