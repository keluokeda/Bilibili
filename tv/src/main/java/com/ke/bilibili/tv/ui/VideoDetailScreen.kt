package com.ke.bilibili.tv.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.ke.biliblli.viewmodel.VideoDetailAction
import com.ke.biliblli.viewmodel.VideoDetailState
import com.ke.biliblli.viewmodel.VideoDetailViewModel
import com.orhanobut.logger.Logger

@OptIn(UnstableApi::class)
@Composable
internal fun VideoDetailRoute() {
    val viewModel = hiltViewModel<VideoDetailViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VideoDetailScreen(uiState, {
        viewModel.handleAction(VideoDetailAction.Retry)
    }, {
        viewModel.handleAction(VideoDetailAction.ShowController(it))
    })


}

@Composable
private fun VideoDetailScreen(
    uiState: VideoDetailState,
    retry: () -> Unit,
    setControllerVisible: (Boolean) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {
            is VideoDetailState.Content -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .onKeyEvent {
                            if (it.key == Key.DirectionCenter && it.type == KeyEventType.KeyUp) {
                                Logger.d("切换菜单显示")
                                setControllerVisible(!uiState.showController)
                                true
                            }
                            false
                        }
                        .focusable(), contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { context ->
                            PlayerView(context).apply {
                                player = uiState.player
                                useController = false
                            }

                        }, modifier = Modifier
                            .fillMaxSize()

                    )

                    if (uiState.playbackState == Player.STATE_BUFFERING) {
                        Text("加载中")
                    }

                    if (uiState.showController) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Black.copy(alpha = 0.6f))
                                .padding(16.dp)

                        ) {
                            Text("视频标题")
                        }
                    }
                }


            }

            VideoDetailState.Error -> {
                Button(onClick = retry) {
                    Text("出错了，点我重试")
                }
            }

            VideoDetailState.Loading -> {
                Text("加载中...")
            }
        }
    }
}