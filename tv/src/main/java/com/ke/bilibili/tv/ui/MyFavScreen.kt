package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.bilibili.tv.viewmodel.MyFavAction
import com.ke.bilibili.tv.viewmodel.MyFavState
import com.ke.bilibili.tv.viewmodel.MyFavViewModel
import com.ke.biliblli.api.response.FavResourceMediaResponse
import com.ke.biliblli.api.response.UserFavResponse
import com.ke.biliblli.common.Screen

@Composable
fun MyFavRoute(navigate: (Any) -> Unit) {
    val viewModel = hiltViewModel<MyFavViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val mediaList = viewModel.listFlow.collectAsLazyPagingItems()

    viewModel.event.observeWithLifecycle {
        mediaList.refresh()
    }

    MyFavScreen(state, mediaList, {
        viewModel.handleAction(MyFavAction.ClickFav(it))
        mediaList.refresh()
    }, navigate)
}

@Composable
private fun MyFavScreen(
    uiState: MyFavState,
    mediaList: LazyPagingItems<FavResourceMediaResponse>,
    onClickFav: (UserFavResponse) -> Unit,
    navigate: (Any) -> Unit
) {


    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .width(120.dp)
        ) {
            items(uiState.list) {
                ListItem(selected = it == uiState.selected, onClick = {
                    onClickFav(it)
                }, headlineContent = {
                    Text(it.title)
                })
            }
        }

        if (uiState.selected != null) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                items(mediaList.itemCount) {
                    val item = mediaList[it]!!
                    Card(onClick = {
                        navigate(Screen.VideoDetail(item.bvid))
                    }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9f)
                        ) {
                            AsyncImage(
                                model = item.cover,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(item.title, maxLines = 2, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}