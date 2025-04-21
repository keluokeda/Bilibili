package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ke.bilibili.tv.viewmodel.SearchAction
import com.ke.bilibili.tv.viewmodel.SearchState
import com.ke.bilibili.tv.viewmodel.SearchViewModel
import com.ke.biliblli.api.response.SearchResponse
import com.ke.biliblli.common.Screen
import com.ke.biliblli.common.entity.VideoViewEntity


@Composable
internal fun SearchRoute(navigate: (Any) -> Unit) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resultList = viewModel.resultList.collectAsLazyPagingItems()

    SearchScreen(viewModel.showKeyboardWhenStart, uiState, {
        if (it.isEmpty()) {
            resultList.refresh()
        }
        viewModel.handleAction(SearchAction.KeywordsChanged(it))
    }, resultList, {
        resultList.refresh()
    }, navigate)
}

@Composable
private fun SearchScreen(
    showKeyboardWhenStart: Boolean,
    uiState: SearchState,
    onTextChanged: (String) -> Unit,
    resultList: LazyPagingItems<SearchResponse>,
    search: () -> Unit,
    navigate: (Any) -> Unit
) {

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .weight(3f)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    uiState.keywords, onValueChange = {
                        onTextChanged(it)
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    maxLines = 1,
                    textStyle = TextStyle(color = Color.White)
                )
                IconButton(onClick = search, enabled = uiState.keywords.isNotEmpty()) {
                    Icon(Icons.Default.Search, null)
                }
            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                items(uiState.suggestWords) {
                    ListItem(selected = false, onClick = {
                        onTextChanged(it.value)
                        search()
                    }, headlineContent = {
                        Text(it.value)
                    })
                }
            }

        }


        if (resultList.itemCount == 0) {

            LazyColumn(modifier = Modifier.weight(3f)) {
                item {
                    Text(
                        "热搜",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }

                items(uiState.hotWords) {
                    ListItem(selected = false, onClick = {
                        onTextChanged(it.keyword)
                        search()
                    }, headlineContent = {
                        Text(it.showName)
                    })
                }
            }

            LazyColumn(modifier = Modifier.weight(4f)) {
                item {
                    Text(
                        "历史搜索",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }

                items(uiState.historyList) {
                    ListItem(selected = false, onClick = {
                        onTextChanged(it)
                        search()
                    }, headlineContent = {
                        Text(it)
                    })
                }
            }

        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(24.dp),
                modifier = Modifier
                    .weight(7f)
                    .fillMaxHeight()
            ) {
                items(resultList.itemCount) {
                    val item = resultList[it]!!


                    VideoItem(
                        VideoViewEntity(
                            pic = "https:" + item.pic,
                            view = item.play,
                            danmaku = item.danmaku,
                            duration = item.duration().toLong(),
                            title =
                                item.title.replace("<em class=\"keyword\">", "")
                                    .replace("</em>", ""),
                            author = item.author
                        )
                    ) {
                        navigate(Screen.VideoInfo(item.bvid))
                    }
                }
            }
        }


    }


}