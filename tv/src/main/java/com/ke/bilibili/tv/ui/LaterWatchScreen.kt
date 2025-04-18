package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.ke.biliblli.common.Screen
import com.ke.biliblli.common.entity.VideoViewEntity
import com.ke.biliblli.viewmodel.LaterWatchState
import com.ke.biliblli.viewmodel.LaterWatchViewModel


@Composable
internal fun LaterWatchRoute(
    toDetail: (Any) -> Unit
) {
    val viewModel = hiltViewModel<LaterWatchViewModel>()

    val state by viewModel.uiState.collectAsStateWithLifecycle()



    LaterWatchScreen(state, {
        viewModel.handleAction(Unit)
    }, toDetail)
}

@Composable
private fun LaterWatchScreen(
    state: LaterWatchState,
    retry: () -> Unit,
    toDetail: (Any) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            LaterWatchState.Error -> {
                Button(onClick = retry) {
                    Text("出错了，点我重试")
                }
            }

            LaterWatchState.Loading -> {
                Text("加载中")
            }

            is LaterWatchState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = 4),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(24.dp), modifier = Modifier.fillMaxSize()
                ) {
                    items(state.list) {

                        val entity = VideoViewEntity(
                            it.pic,
                            it.stat.view,
                            it.stat.danmaku,
                            it.duration,
                            it.title,
                            it.owner.name
                        )
                        VideoItem(entity) {
                            toDetail(Screen.VideoInfo(it.bvid))
                        }
                    }
                }
            }
        }


    }
}