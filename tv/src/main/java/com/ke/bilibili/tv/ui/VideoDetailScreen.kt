package com.ke.bilibili.tv.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.tv.material3.AssistChip
import androidx.tv.material3.AssistChipDefaults
import androidx.tv.material3.Button
import androidx.tv.material3.ClickableChipColors
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.FilterChip
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.biliblli.viewmodel.VideoDetailAction
import com.ke.biliblli.viewmodel.VideoDetailEvent
import com.ke.biliblli.viewmodel.VideoDetailState
import com.ke.biliblli.viewmodel.VideoDetailViewModel
import com.ke.biliblli.viewmodel.VideoInfoViewModel
import com.ke.biliblli.viewmodel.VideoResolution
import com.orhanobut.logger.Logger

@OptIn(UnstableApi::class)
@Composable
internal fun VideoDetailRoute() {
    val viewModel = hiltViewModel<VideoDetailViewModel>()

    val videoInfoViewModel = hiltViewModel<VideoInfoViewModel>()


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VideoDetailScreen(
        uiState,
        {
            viewModel.handleAction(VideoDetailAction.Retry)
        },
        {
            viewModel.handleAction(VideoDetailAction.ShowController(it))
        }, {
            viewModel.handleAction(VideoDetailAction.UpdateVideoResolution(it))
        }
    )


}

@kotlin.OptIn(ExperimentalLayoutApi::class, ExperimentalTvMaterial3Api::class)
@Composable
private fun VideoDetailScreen(
    uiState: VideoDetailState,
    retry: () -> Unit,
    setControllerVisible: (Boolean) -> Unit,
    updateResolution: (VideoResolution) -> Unit
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
                        .focusable(!uiState.showController), contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { context ->
                            PlayerView(context).apply {
                                player = uiState.player
                                useController = false
                            }

                        }, modifier = Modifier
                            .fillMaxSize()
                            .focusable(false)
                            .background(Color.Black)

                    )

                    if (uiState.playbackState == Player.STATE_BUFFERING) {
                        Text("加载中")
                    }

                    if (uiState.showController) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Black.copy(alpha = 0.6f))
                                .padding(16.dp),

                            ) {
                            Spacer(modifier = Modifier.weight(1f))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "视频分辨率",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                                )

                                uiState.videoResolutionList.forEach { resolution ->
                                    FilterChip(
                                        selected = resolution == uiState.currentVideoResolution,
                                        onClick = {
                                            updateResolution(resolution)
                                        }, enabled = resolution != uiState.currentVideoResolution
                                    ) {
                                        Text(resolution.text)
                                    }

                                }
                            }

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