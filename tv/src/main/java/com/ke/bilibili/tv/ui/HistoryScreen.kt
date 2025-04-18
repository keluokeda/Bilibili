package com.ke.bilibili.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.biliblli.api.response.HistoryItem
import com.ke.biliblli.common.Screen
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

            Card(onClick = {
                navigate(Screen.VideoInfo(item.history.bvid))
            }) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f), contentAlignment = Alignment.BottomCenter
                ) {
                    AsyncImage(
                        model = item.cover,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        item.title, maxLines = 1, style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        ), modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.Black.copy(
                                    alpha = 0.3f
                                )
                            )
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}