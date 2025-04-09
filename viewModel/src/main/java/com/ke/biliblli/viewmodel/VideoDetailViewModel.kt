package com.ke.biliblli.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.navigation.toRoute
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val minRatio = 1.0f

@UnstableApi
@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository,
    @ApplicationContext context: Context
) : BaseViewModel<VideoDetailState, VideoDetailAction, VideoDetailEvent>(VideoDetailState.Loading) {
    private val params = savedStateHandle.toRoute<Screen.VideoDetail>()

    val dataSourceFactory: DataSource.Factory =
        DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 13_3_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.4 Safari/605.1.15")
            .setDefaultRequestProperties(
                mapOf(
                    "referer" to "https://www.bilibili.com"
                )
            )

    private val playerListener = object : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            val ratio = videoSize.width.toFloat() / videoSize.height.toFloat()

            (uiState.value as? VideoDetailState.Content)?.copy(
                ratio = if (ratio < minRatio) minRatio else ratio
            )?.apply {
                _uiState.value = this
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {

            (uiState.value as? VideoDetailState.Content)?.copy(
                isPlaying = isPlaying
            )?.apply {
                _uiState.value = this
            }
        }
    }

    private val player = ExoPlayer.Builder(context)
        .build().apply {
            playWhenReady = true
            addListener(playerListener)
        }

    /**
     * 播放视频
     */
    private fun play(videoUrl: String, audioUrl: String) {
        val videoSource: MediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)

                .createMediaSource(MediaItem.fromUri(videoUrl))
        val audioSource: MediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(audioUrl))

        val mergeSource: MediaSource =
            MergingMediaSource(videoSource, audioSource)
        player.setMediaSource(mergeSource)
        player.prepare()
    }

    init {

        refresh()

        viewModelScope.launch {
            while (true) {
                (uiState.value as? VideoDetailState.Content)?.copy(
                    currentPosition = player.currentPosition
                )?.apply {
                    _uiState.value = this
                }


                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.removeListener(playerListener)
        player.release()
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = VideoDetailState.Loading

            try {
                val response = bilibiliRepository.videoUrl(params.cid, params.bvid).data!!

                response.dash.run {
                    val videoUrl = video.first().baseUrl
                    val audioUrl = audio.first().baseUrl
                    _uiState.value = VideoDetailState.Content(player)
                    play(videoUrl, audioUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = VideoDetailState.Error
            }
        }
    }

    override fun handleAction(action: VideoDetailAction) {

        when (action) {
            VideoDetailAction.Retry -> {
                refresh()
            }

            is VideoDetailAction.SetFullScreen -> {
                _uiState.update {
                    (it as VideoDetailState.Content).copy(isFullScreen = action.isFullScreen)
                }
            }

            is VideoDetailAction.ShowController -> {
                if (action.show) {
                    _uiState.update {
                        (it as VideoDetailState.Content).copy(showController = true)
                    }

                    hideController(false)

                } else {
//                    _uiState.update {
//                        (it as VideoDetailState.Content).copy(showController = false)
//                    }
                    hideController(true)
                }


            }

            is VideoDetailAction.PlayPauseVideo -> {
                if (action.play) {
                    player.play()
                } else {
                    player.pause()
                }
            }


            is VideoDetailAction.OnSliderValueChangeFinished -> {
                player.seekTo(action.progress)
            }
        }
    }


    private var hideControllerJob: Job? = null

    private fun hideController(now: Boolean) {
        hideControllerJob?.cancel()
        hideControllerJob =
            viewModelScope.launch {
                delay(
                    if (now) 0 else
                        5000
                )

                _uiState.update {
                    (it as VideoDetailState.Content).copy(showController = false)
                }
            }
    }

}


sealed interface VideoDetailState {
    data object Loading : VideoDetailState
    data object Error : VideoDetailState
    data class Content(
        val player: ExoPlayer,
        val isFullScreen: Boolean = false,
        val ratio: Float = 16 / 9.0f,
        val showController: Boolean = false,
        val isPlaying: Boolean = true,
        val currentPosition: Long = 0
    ) :
        VideoDetailState
}

sealed interface VideoDetailAction {

    data object Retry : VideoDetailAction

    data class SetFullScreen(val isFullScreen: Boolean) : VideoDetailAction

//    data class OnVideoSizeChanged(val width: Int, val height: Int) : VideoDetailAction

    data class ShowController(val show: Boolean) : VideoDetailAction

    data class PlayPauseVideo(val play: Boolean) : VideoDetailAction


//    data class OnPositionChanged(val position: Long) : VideoDetailAction

    data class OnSliderValueChangeFinished(val progress: Long) : VideoDetailAction
}

sealed interface VideoDetailEvent {

}