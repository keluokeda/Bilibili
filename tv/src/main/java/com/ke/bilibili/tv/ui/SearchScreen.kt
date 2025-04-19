package com.ke.bilibili.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
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

    SearchScreen(uiState, {
        viewModel.handleAction(SearchAction.KeywordsChanged(it))
    }, resultList, {
        resultList.refresh()
    }, navigate)
}

@Composable
private fun SearchScreen(
    uiState: SearchState,
    onTextChanged: (String) -> Unit,
    resultList: LazyPagingItems<SearchResponse>,
    search: () -> Unit,
    navigate: (Any) -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.3f))
                .clip(RoundedCornerShape(3.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                uiState.keywords, onValueChange = {
                    onTextChanged(it)
                }, modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                maxLines = 1

            )

            IconButton(onClick = search, enabled = uiState.keywords.isNotEmpty()) {
                Icon(Icons.Default.Search, null)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(24.dp)
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