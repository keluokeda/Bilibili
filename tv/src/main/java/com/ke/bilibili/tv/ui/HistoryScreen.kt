package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.bilibili.tv.ui.component.VideoItem
import com.ke.bilibili.tv.ui.component.VideoItemView
import com.ke.biliblli.api.response.HistoryItem
import com.ke.biliblli.common.Screen
import com.ke.biliblli.common.duration
import com.ke.biliblli.viewmodel.HistoryViewModel


@Composable
fun HistoryRoute(
    state: LazyGridState,
    navigate: (Any) -> Unit
) {
    val viewModel = hiltViewModel<HistoryViewModel>()
    val list = viewModel.listFlow.collectAsLazyPagingItems()


    viewModel.event.observeWithLifecycle {
        state.scrollToItem(0)
        list.refresh()
    }


    HistoryScreen(state, list, navigate)
}

@Composable
private fun HistoryScreen(
    state: LazyGridState,
    list: LazyPagingItems<HistoryItem>,
    navigate: (Any) -> Unit
) {
    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        items(list.itemCount) {
            val item = list[it]!!

            VideoItemView(
                VideoItem(
                    title = item.title,
                    image = item.cover,
                    view = "-",
                    danmaku = "-",
                    duration = item.duration.duration(),
                    lastProgress = item.progress.duration(),
                    author = item.authorName
                )
            ) {
                navigate(Screen.VideoInfo(item.history.bvid))
            }

//            Card(onClick = {
//                navigate(Screen.VideoInfo(item.history.bvid))
//            }) {
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(16 / 9f), contentAlignment = Alignment.BottomCenter
//                ) {
//                    AsyncImage(
//                        model = item.cover,
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Text(
//                        item.title, maxLines = 1, style = MaterialTheme.typography.bodyMedium.copy(
//                            color = Color.White
//                        ), modifier = Modifier
//                            .fillMaxWidth()
//                            .background(
//                                color = Color.Black.copy(
//                                    alpha = 0.3f
//                                )
//                            )
//                            .padding(4.dp)
//                    )
//                }
        }
    }
}
