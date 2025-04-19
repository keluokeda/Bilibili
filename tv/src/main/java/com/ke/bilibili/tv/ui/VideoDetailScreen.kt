package com.ke.bilibili.tv.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.FilterChip
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.bilibili.tv.ui.component.DanmakuItem
import com.ke.bilibili.tv.ui.component.DanmakuView
import com.ke.biliblli.common.entity.DanmakuPosition
import com.ke.biliblli.viewmodel.AudioResolution
import com.ke.biliblli.viewmodel.Resolution
import com.ke.biliblli.viewmodel.VideoDetailAction
import com.ke.biliblli.viewmodel.VideoDetailEvent
import com.ke.biliblli.viewmodel.VideoDetailState
import com.ke.biliblli.viewmodel.VideoDetailViewModel
import com.orhanobut.logger.Logger

@OptIn(UnstableApi::class)
@Composable
internal fun VideoDetailRoute() {
    val viewModel = hiltViewModel<VideoDetailViewModel>()


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var sender by remember {
        mutableStateOf<((DanmakuItem) -> Unit)?>(null)
    }

    viewModel.event.observeWithLifecycle {
        when (it) {
            is VideoDetailEvent.ShootDanmaku -> {
                val item = DanmakuItem(
                    id = it.item.id.toString(),
//                    color = it.item.color,
                    color = Color.Black.value.toInt(),
                    fontSize = it.item.fontSize,
                    content = it.item.content
                )
                sender?.invoke(item)
            }

            is VideoDetailEvent.ShowVideoResolutionListDialog -> {

            }
        }
    }



    VideoDetailScreen(
        uiState,
        {
            viewModel.handleAction(VideoDetailAction.Retry)
        },
        {
            viewModel.handleAction(VideoDetailAction.ShowController(it, 10000))
        }, {
            viewModel.handleAction(VideoDetailAction.UpdateVideoResolution(it))
        }, {
            viewModel.handleAction(VideoDetailAction.UpdateAudioResolution(it))
        },
        {
            sender = it
        }, {
            if (it) {
                viewModel.handleAction(VideoDetailAction.StartSpeedPlay)
            } else {
                viewModel.handleAction(VideoDetailAction.StopSpeedPlay)
            }
        }, {

            viewModel.handleAction(VideoDetailAction.TogglePlaying)
        }, {
            viewModel.handleAction(VideoDetailAction.Backward)
        })


}

@kotlin.OptIn(ExperimentalLayoutApi::class, ExperimentalTvMaterial3Api::class)
@Composable
private fun VideoDetailScreen(
    uiState: VideoDetailState,
    retry: () -> Unit,
    setControllerVisible: (Boolean) -> Unit,
    updateVideoResolution: (Resolution) -> Unit,
    updateAudioResolution: (AudioResolution) -> Unit,
    receiver: ((DanmakuItem) -> Unit) -> Unit,
    setSpeedPlay: (Boolean) -> Unit,
    togglePlay: () -> Unit,
    backward: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {


            is VideoDetailState.Content -> {
                BackHandler(enabled = uiState.showController) {
                    setControllerVisible(false)
                }

                Box(
                    modifier = if (uiState.showController) {
                        Modifier
                            .fillMaxSize()

                    } else {
                        Modifier
                            .fillMaxSize()
                            .onKeyEvent {
                                Logger.d(it)
                                Logger.d(it.key.toString())

                                if (it.key == Key.Menu && it.type == KeyEventType.KeyUp) {
                                    setControllerVisible(!uiState.showController)
                                    true
                                } else if (it.key == Key.DirectionRight && it.type == KeyEventType.KeyDown) {
                                    setSpeedPlay(true)
                                    true
                                } else if (it.key == Key.DirectionRight && it.type == KeyEventType.KeyUp) {
                                    setSpeedPlay(false)
                                    true
                                } else if (it.key == Key.DirectionCenter && (it.type == KeyEventType.KeyUp)) {
                                    togglePlay()
                                    true
                                } else if (it.key == Key.DirectionLeft && it.type == KeyEventType.KeyUp) {
                                    backward()
                                    true
                                }
                                false
                            }
                            .focusable()
                    },
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { context ->
                            PlayerView(context).apply {
                                player = uiState.player
                                useController = false
                            }

                        }, modifier = Modifier
                            .fillMaxSize()
//                            .focusable(false)
                            .background(Color.Black)

                    )

                    if (uiState.danmakuEnable) {


                        Box(
                            modifier = when (uiState.danmakuPosition) {
                                DanmakuPosition.Full -> Modifier.fillMaxSize()
                                DanmakuPosition.Top2 -> Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(2f)

                                DanmakuPosition.Top3 -> Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(3f)

                                DanmakuPosition.Top4 -> Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(4f)

                                DanmakuPosition.Top5 -> Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(5f)

                                DanmakuPosition.Top6 -> Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(6f)
                            }.align(Alignment.TopCenter)
                        ) {
                            DanmakuView(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                receiver(it)
                            }
                        }


                    }





                    if (uiState.playbackState == Player.STATE_BUFFERING) {
                        Text("加载中")
                    }

                    if (uiState.showController) {

                        val focusRequester = remember { FocusRequester() }

                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Black.copy(alpha = 0.6f))
                                .padding(16.dp)
                                .focusable()
                                .focusRequester(focusRequester),

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
                                            updateVideoResolution(resolution)
                                        }, enabled = resolution != uiState.currentVideoResolution
                                    ) {
                                        Text(resolution.videoResolution.displayName)
                                    }

                                }
                            }

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "音频音质",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                                )

                                uiState.audioResolutionList.forEach { resolution ->
                                    FilterChip(
                                        selected = resolution == uiState.currentAudioResolution,
                                        onClick = {
                                            updateAudioResolution(resolution)
                                        }, enabled = resolution != uiState.currentAudioResolution
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