package com.ke.biliblli.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.navigation.toRoute
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.common.Screen
import com.ke.biliblli.common.entity.BilibiliDanmaku
import com.ke.biliblli.common.entity.DanmakuDensity
import com.ke.biliblli.common.entity.DanmakuFontSize
import com.ke.biliblli.common.entity.DanmakuPosition
import com.ke.biliblli.common.entity.DanmakuSpeed
import com.ke.biliblli.common.entity.VideoResolution
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs


private const val minRatio = 1.0f


val audioResolutionPairList = listOf<Triple<Int, Int, String>>(
    Triple(0, 30216, "64K"),
    Triple(1, 30232, "132K"),
    Triple(2, 30280, "192K"),
    Triple(3, 30250, "杜比全景声"),
    Triple(4, 30251, "Hi-Res无损")
)

@UnstableApi
@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository,
    private val bilibiliStorage: BilibiliStorage,
    @ApplicationContext context: Context,

    ) :
    BaseViewModel<VideoDetailState, VideoDetailAction, VideoDetailEvent>(VideoDetailState.Loading) {

    private val params = savedStateHandle.toRoute<Screen.VideoDetail>()


    private val _config = MutableStateFlow(
        DanmakuTextConfig(
            bilibiliStorage.danmakuFontSize,
            bilibiliStorage.danmakuColorful,
            bilibiliStorage.danmakuSpeed
        )
    )

    val config = _config.asStateFlow()

    private val dataSourceFactory: DataSource.Factory =
        DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 13_3_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.4 Safari/605.1.15")
            .setDefaultRequestProperties(
                mapOf(
                    "referer" to "https://www.bilibili.com"
                )
            )

    private val analyticsListener = object : AnalyticsListener {
        override fun onTracksChanged(
            eventTime: AnalyticsListener.EventTime,
            tracks: Tracks
        ) {
            super.onTracksChanged(eventTime, tracks)

//            tracks.groups.forEach {
//                if (it.type == C.TRACK_TYPE_VIDEO) {
//                    if (it.isTrackSelected(0)) {
//                        val format = it.getTrackFormat(0)
//                        Logger.d("format = ${format.bitrate}")
//                    }
//                }
//            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
//            Logger.d("视频分辨率变化 ${videoSize.width} ${videoSize.height}")

            if (videoSize.height <= 0) {
                return
            }
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

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
//            Logger.d("onPlaybackStateChanged $playbackState")

            if (playbackState == Player.STATE_ENDED) {
                viewModelScope.launch {
                    _event.send(VideoDetailEvent.BackToInfo)
                }
            }

            (uiState.value as? VideoDetailState.Content)?.copy(
                playbackState = playbackState
            )?.apply {
                _uiState.value = this
            }
        }


    }

    private fun loadDanmakuList(id: Long) {
        viewModelScope.launch {
            try {
                val danmakuList = bilibiliRepository.danmakuList(1, id, 1)
                danmakuList.forEachIndexed { index, item ->
                    shootDanmaku(index, item)
                }
            } catch (e: Exception) {
                CrashHandler.handler(e)

            }

        }

    }

    private val danmakuDensity = bilibiliStorage.danmakuDensity

    private fun shootDanmaku(index: Int, danmaku: BilibiliDanmaku) {

        if (danmakuDensity == DanmakuDensity.Half && index % 2 != 0) {
            return
        } else if (danmakuDensity == DanmakuDensity.Less && index % 3 != 0) {
            return
        }

        viewModelScope.launch {
            delay(danmaku.progress.toLong())
            _event.send(VideoDetailEvent.ShootDanmaku(danmaku))
        }
    }

    private val renderersFactory =
        DefaultRenderersFactory(context).forceEnableMediaCodecAsynchronousQueueing()

    private val player = ExoPlayer.Builder(context, renderersFactory)
        .build().apply {
            playWhenReady = true
            addListener(playerListener)
            addAnalyticsListener(analyticsListener)


        }

    /**
     * 播放视频
     */
    private fun play(videoUrl: String, audioUrl: String, startPositionMs: Long = 0) {
        val videoSource: MediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)

                .createMediaSource(
//                    MediaItem.fromUri(videoUrl)
//                        .apply {
//
//                        }
                    MediaItem.Builder()
                        .setUri(videoUrl)
                        .setMimeType(MimeTypes.APPLICATION_MPD)
                        .build()
                )

        val audioSource: MediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(audioUrl))

        val mergeSource: MediaSource =
            MergingMediaSource(videoSource, audioSource)
        player.setMediaSource(mergeSource, startPositionMs)
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

                    if (currentPosition != 0L) {
                        bilibiliRepository.reportHistory(
                            params.aid,
                            params.cid,
                            currentPosition / 1000
                        )
                    }
                }


                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.removeListener(playerListener)
        player.removeAnalyticsListener(analyticsListener)
        player.release()
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = VideoDetailState.Loading

            try {
//                val view = bilibiliRepository.videoView(params.bvid).data!!


                val response = bilibiliRepository.videoUrl(params.cid, params.bvid).data!!


                loadDanmakuList(params.cid)



                response.dash.run {

                    val videoList = video.map { dashVideo ->
                        val text = VideoResolution.entries.first {
                            it.code == dashVideo.id
                        }

                        Resolution(text, dashVideo.baseUrl)
                    }.distinctBy {
                        it.videoResolution.code
                    }.sortedByDescending { it.videoResolution.code }


//                    Logger.d(audio.map {
//                        it.id
//                    })

                    val audioList = (audio + (dolby?.audio ?: emptyList())).map { audio ->
                        val triple = audioResolutionPairList.first {
                            it.second == audio.id
                        }

                        AudioResolution(audio.id, triple.third, audio.baseUrl) to triple.first
                    }.sortedByDescending {
                        it.second
                    }.map {
                        it.first
                    }
                        .distinctBy {
                            it.id
                        }

                    _uiState.value = VideoDetailState.Content(
                        player,
                        videoResolutionList = videoList,
                        currentVideoResolution = videoList.firstOrNull {
                            it.videoResolution == bilibiliStorage.videoResolution
                        } ?: videoList.first(),
                        audioResolutionList = audioList,
                        currentAudioResolution = audioList.first(),
                        danmakuEnable = bilibiliStorage.danmakuEnable,
                        danmakuPosition = bilibiliStorage.danmakuPosition
                    ).apply {
                        play(
                            currentVideoResolution.url,
                            audioList.first().url,
                            startPositionMs = if (abs(response.dash.duration - response.lastPlayTime / 1000) < 5) 0 else response.lastPlayTime
                        )
                    }

                }

            } catch (e: Exception) {
//                e.printStackTrace()
                CrashHandler.handler(e)
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

                    hideController(false, action.duration)

                } else {
//                    _uiState.update {
//                        (it as VideoDetailState.Content).copy(showController = false)
//                    }
                    hideController(true, action.duration)
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

            VideoDetailAction.ShowVideoResolutionListDialog -> {
//                viewModelScope.launch {
//                    (uiState.value as? VideoDetailState.Content)?.apply {
//                        _event.send(
//                            ShowVideoResolutionListDialog(
//                                videoResolutionList,
//                                currentVideoResolution
//                            )
//                        )
//                    }
//                }
            }

            is VideoDetailAction.UpdateVideoResolution -> {


                _uiState.update {

                    (it as VideoDetailState.Content).copy(currentVideoResolution = action.newValue)
                        .apply {
                            play(action.newValue.url, currentAudioResolution.url, currentPosition)
                        }
                }
            }

            is VideoDetailAction.UpdateAudioResolution -> {
                _uiState.update {

                    (it as VideoDetailState.Content).copy(currentAudioResolution = action.newValue)
                        .apply {
                            play(currentVideoResolution.url, action.newValue.url, currentPosition)
                        }
                }
            }

            VideoDetailAction.StartSpeedPlay -> {
                player.setPlaybackSpeed(5f)
                _uiState.update {
                    (it as VideoDetailState.Content).copy(
                        showProgress = true,
                        showController = false
                    )
                }
//                hideController(false, 5000)
            }

            VideoDetailAction.StopSpeedPlay -> {
                player.setPlaybackSpeed(1f)
                _uiState.update {
                    (it as VideoDetailState.Content).copy(showProgress = false)
                }
//                hideController(false, 5000)
            }

            VideoDetailAction.TogglePlaying -> {


                val isPlaying = player.isPlaying
                if (isPlaying) {
                    player.pause()
//                    _uiState.update {
//                        (it as VideoDetailState.Content).copy(showController = true)
//                    }
//                    hideController(false, Long.MAX_VALUE)
                } else {
                    player.play()
//                    _uiState.update {
//                        (it as VideoDetailState.Content).copy(showController = false)
//                    }
//                    hideController(true, 0)
                }
            }

            VideoDetailAction.Backward -> {
                val target = player.currentPosition - 10000
                player.seekTo(if (target > 0) target else 0)
            }
        }
    }


    private var hideControllerJob: Job? = null

    private fun hideController(now: Boolean, duration: Long) {
        hideControllerJob?.cancel()
        hideControllerJob =
            viewModelScope.launch {
                delay(
                    if (now) 0 else
                        duration
                )

                _uiState.update {
                    (it as VideoDetailState.Content).copy(showController = false)
                }
            }
    }

}

data class DanmakuTextConfig(
    val fontSize: DanmakuFontSize,
    val colorful: Boolean,
    val speed: DanmakuSpeed
)

data class Resolution(
    val videoResolution: VideoResolution,
    val url: String
)


data class AudioResolution(
    val id: Int,
    val text: String,
    val url: String
)


sealed interface VideoDetailState {
    data object Loading : VideoDetailState
    data object Error : VideoDetailState
    data class Content(
        val player: ExoPlayer,
        val videoResolutionList: List<Resolution>,
        val currentVideoResolution: Resolution,
        val audioResolutionList: List<AudioResolution>,
        val currentAudioResolution: AudioResolution,
        val danmakuEnable: Boolean,
        val danmakuPosition: DanmakuPosition,
        val isFullScreen: Boolean = false,
        val ratio: Float = 16 / 9.0f,
        val showController: Boolean = false,
        val showProgress: Boolean = false,
        val isPlaying: Boolean = true,
        val currentPosition: Long = 0,
        val playbackState: Int = Player.STATE_BUFFERING
    ) :
        VideoDetailState
}

sealed interface VideoDetailAction {

    data object Retry : VideoDetailAction

    data class SetFullScreen(val isFullScreen: Boolean) : VideoDetailAction

//    data class OnVideoSizeChanged(val width: Int, val height: Int) : VideoDetailAction

    data class ShowController(val show: Boolean, val duration: Long = 5000) : VideoDetailAction

    data class PlayPauseVideo(val play: Boolean) : VideoDetailAction


//    data class OnPositionChanged(val position: Long) : VideoDetailAction

    data class OnSliderValueChangeFinished(val progress: Long) : VideoDetailAction

    data object ShowVideoResolutionListDialog : VideoDetailAction

    data class UpdateVideoResolution(val newValue: Resolution) : VideoDetailAction

    data class UpdateAudioResolution(val newValue: AudioResolution) : VideoDetailAction

    data object StartSpeedPlay : VideoDetailAction

    data object StopSpeedPlay : VideoDetailAction

    data object TogglePlaying : VideoDetailAction

    data object Backward : VideoDetailAction
}

sealed interface VideoDetailEvent {

    data class ShowVideoResolutionListDialog(
        val list: List<VideoResolution>,
        val current: VideoResolution
    ) : VideoDetailEvent

    data class ShootDanmaku(val item: BilibiliDanmaku) : VideoDetailEvent

    data object BackToInfo : VideoDetailEvent
}