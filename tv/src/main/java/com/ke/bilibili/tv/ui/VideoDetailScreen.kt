package com.ke.bilibili.tv.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.bilibili.tv.ui.component.DanmakuView
import com.ke.bilibili.tv.ui.component.Loading
import com.ke.bilibili.tv.ui.component.ProgressBar
import com.ke.biliblli.common.duration
import com.ke.biliblli.viewmodel.AudioResolution
import com.ke.biliblli.viewmodel.Resolution
import com.ke.biliblli.viewmodel.VideoDetailAction
import com.ke.biliblli.viewmodel.VideoDetailEvent
import com.ke.biliblli.viewmodel.VideoDetailState
import com.ke.biliblli.viewmodel.VideoDetailViewModel
import com.ke.biliblli.viewmodel.VideoSpeed

@OptIn(UnstableApi::class)
@Composable
internal fun VideoDetailRoute(
    back: () -> Unit
) {
    val viewModel = hiltViewModel<VideoDetailViewModel>()


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    val config by viewModel.config.collectAsStateWithLifecycle()

//    var sender by remember {
//        mutableStateOf<((DanmakuItem) -> Unit)?>(null)
//    }

    viewModel.event.observeWithLifecycle {
        when (it) {
//            is VideoDetailEvent.ShootDanmaku -> {
//                val item = DanmakuItem(
//                    id = it.item.id.toString(),
//                    color = it.item.rgb(config.colorful),
//                    fontSize = (it.item.fontSize * config.fontSize.ratio).toInt(),
//                    content = it.item.content,
//                    duration = config.speed.duration
//                )
//                sender?.invoke(item)
//            }

            is VideoDetailEvent.ShowVideoResolutionListDialog -> {

            }

            VideoDetailEvent.BackToInfo -> {
                back()
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
            viewModel.handleAction(VideoDetailAction.Forward)
        }, {

            viewModel.handleAction(VideoDetailAction.TogglePlaying)
        }, {
            viewModel.handleAction(VideoDetailAction.Backward)
        }, {
            viewModel.handleAction(VideoDetailAction.UpdateSpeed(it))
        }, {
            viewModel.handleAction(VideoDetailAction.SetDanmakuVisible(it))
        })


}

@SuppressLint("UnusedBoxWithConstraintsScope")
@kotlin.OptIn(ExperimentalLayoutApi::class, ExperimentalTvMaterial3Api::class)
@Composable
private fun VideoDetailScreen(
    uiState: VideoDetailState,
    retry: () -> Unit,
    setControllerVisible: (Boolean) -> Unit,
    updateVideoResolution: (Resolution) -> Unit,
    updateAudioResolution: (AudioResolution) -> Unit,
    forward: () -> Unit,
    togglePlay: () -> Unit,
    backward: () -> Unit,
    updateSpeed: (VideoSpeed) -> Unit,
    setDanmakuVisible: (Boolean) -> Unit
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

                                return@onKeyEvent if ((it.key == Key.Menu || it.key == Key.DirectionDown) && it.type == KeyEventType.KeyUp) {
                                    setControllerVisible(true)
                                    true
                                } else if (it.key == Key.DirectionRight && it.type == KeyEventType.KeyUp) {
                                    forward()
                                    true
                                } else if (it.key == Key.DirectionCenter && (it.type == KeyEventType.KeyUp)) {
                                    togglePlay()
                                    true
                                } else if (it.key == Key.DirectionLeft && it.type == KeyEventType.KeyUp) {
                                    backward()
                                    true
                                } else {
                                    false
                                }

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
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(1f / uiState.danmakuPosition.code.toFloat())
                                    .align(Alignment.TopCenter)

                        ) {

                            DanmakuView()
                        }


                    }


                    12.dp



                    if (uiState.playbackState == Player.STATE_BUFFERING) {
                        Loading()
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

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "倍速",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                                )

                                VideoSpeed.entries.forEach { speed ->
                                    FilterChip(
                                        selected = speed == uiState.videoSpeed,
                                        onClick = {
                                            updateSpeed(speed)
                                        }, enabled = speed != uiState.videoSpeed
                                    ) {
                                        Text(speed.displayName)
                                    }

                                }
                            }

                            ListItem(
                                selected = false,
                                onClick = {
                                    setDanmakuVisible(!uiState.danmakuEnable)
                                },
                                headlineContent = { Text("弹幕开关") }, leadingContent = {
                                    Switch(uiState.danmakuEnable, onCheckedChange = {})
                                })


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                Text(
                                    (uiState.player.currentPosition / 1000).duration(),
                                    color = Color.White
                                )

                                ProgressBar(
                                    modifier = Modifier.weight(1f),
                                    max = uiState.player.duration,
                                    current = uiState.player.currentPosition
                                )
                                Text(
                                    (uiState.player.duration / 1000).duration(),
                                    color = Color.White
                                )


                            }


                        }
                    } else if (uiState.showProgress) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(color = Color.Black.copy(alpha = 0.3f))
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                Text(
                                    (uiState.player.currentPosition / 1000).duration(),
                                    color = Color.White
                                )

                                ProgressBar(
                                    modifier = Modifier.weight(1f),
                                    max = uiState.player.duration,
                                    current = uiState.player.currentPosition
                                )
                                Text(
                                    (uiState.player.duration / 1000).duration(),
                                    color = Color.White
                                )


                            }
                        }
                    } else if (uiState.playerViewShowMiniProgressBar) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .align(Alignment.BottomCenter)
                        ) {
                            ProgressBar(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                max = uiState.player.duration,
                                current = uiState.player.currentPosition
                            )
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