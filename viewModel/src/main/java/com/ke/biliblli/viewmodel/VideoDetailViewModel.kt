package com.ke.biliblli.viewmodel

import android.content.Context
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
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
import com.ke.biliblli.common.entity.DanmakuPosition
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
import kotlin.random.Random


private const val minRatio = 1.0f
const val danmakuItemHeight = 72

val audioResolutionPairList = listOf<Triple<Int, Int, String>>(
    Triple(0, 30216, "64K"),
    Triple(1, 30232, "132K"),
    Triple(2, 30280, "192K"),
    Triple(3, 30250, "杜比全景声"),
    Triple(4, 30251, "Hi-Res无损")
)

enum class VideoSpeed(val speed: Float, val displayName: String) {
    X05(0.5f, "0.5x"),
    X075(0.75f, "0.75x"),
    X1(1f, "1.0x"),
    X125(1.25f, "1.25x"),
    X15(1.5f, "1.5x"),
    X175(1.75f, "1.75x"),
    X2(2.0f, "2.0x")

}

@UnstableApi
@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository,
    private val bilibiliStorage: BilibiliStorage,
    @ApplicationContext context: Context,

    ) :
    BaseViewModel<VideoDetailState, VideoDetailAction, VideoDetailEvent>(VideoDetailState.Loading) {

    private var params = savedStateHandle.toRoute<Screen.VideoDetail>()

    private val playlist = params.playlist.toMutableList()
    private val cidList = params.cidList.toMutableList()


//    private fun canBack(): Boolean{
//        return playlist.isEmpty() && cidList.isEmpty()
//    }

    private val dataSourceFactory: DataSource.Factory =
        DefaultHttpDataSource.Factory()
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 13_3_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.4 Safari/605.1.15")
            .setDefaultRequestProperties(
                mapOf(
                    "referer" to "https://www.bilibili.com"
                )
            )

    private var danmakuViewHeight = 0
    private var danmakuViewWidth = 0

    fun onDanmakuViewGloballyPositioned(coordinates: LayoutCoordinates) {
//        Logger.d("onDanmakuViewGloballyPositioned $coordinates")
        danmakuViewHeight = coordinates.size.height
        danmakuViewWidth = coordinates.size.width
    }

    private val analyticsListener = object : AnalyticsListener {
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
                    if (playlist.isNotEmpty()) {
//                        _event.send(VideoDetailEvent.BackToInfo)
                        loadParamsFromPlaylist(playlist.removeAt(0))
                    } else if (cidList.isNotEmpty()) {
                        params = params.copy(
                            cid = cidList.removeAt(0)
                        )

                        refresh()
                    } else {
                        _event.send(VideoDetailEvent.BackToInfo)
                    }


                }
            }

            (uiState.value as? VideoDetailState.Content)?.copy(
                playbackState = playbackState
            )?.apply {
                _uiState.value = this
            }
        }


    }

    private var currentBvid: String = ""

    private suspend fun loadParamsFromPlaylist(bvid: String) {
        try {
            _uiState.value = VideoDetailState.Loading
            currentBvid = bvid
            val data = bilibiliRepository.videoView(bvid).data!!

            params = Screen.VideoDetail(bvid, data.cid, data.aid)

            initPlayer(data.cid, data.bvid)

        } catch (e: Exception) {
            CrashHandler.handler(e)
            _uiState.value = VideoDetailState.Error
        }
    }

    private val _danmakuItemsForDisplay = MutableStateFlow(listOf<DanmakuItem>())

    val danmakuItemsForDisplay = _danmakuItemsForDisplay.asStateFlow()

    /**
     * @param parentWidth 父组件宽度
     * @param position 在父组件中的位置
     * @param size 自身的大小
     */
    fun onDanmakuSizeMeasured(
        danmakuItem: DanmakuItem,
        parentWidth: Int,
        parentHeight: Int,
        position: IntOffset,
        size: IntSize
    ) {
//        val selfWidth = size.width
        val startTime = player.currentPosition
        val newValue = danmakuItem.copy(
            parentWidth = parentWidth,
            selfWidth = size.height,
            selfHeight = size.height,
            parentHeight = parentHeight,
            offsetX = danmakuViewWidth,
            offsetY = position.y,
            startTime = startTime,
            lastUpdateTime = startTime,
        )

        _danmakuItemsForDisplay.update {
            val list = it.toMutableList()
            val index = list.indexOf(danmakuItem)
            list[index] = newValue
            list
        }
    }

    private val danmakuFontSize = bilibiliStorage.danmakuFontSize
    private val colorful = bilibiliStorage.danmakuColorful
    private val danmakuSpeed = bilibiliStorage.danmakuSpeed
    private val danmakuDensity = bilibiliStorage.danmakuDensity
    private fun onVideoPositionChanged(currentPosition: Long) {

        if (player.duration <= 0) {
            return
        }

        val targetList = _bilibiliDanmakuList.filter {
            it.progress - currentPosition in 0..1000
        }
        _bilibiliDanmakuList.removeAll(targetList)

        //把 targetList 塞进去

//        Logger.d("duration = ${player.duration} , current = $currentPosition")

        _danmakuItemsForDisplay.update { list ->

            val oldList = list.filter {
                currentPosition - it.startTime < danmakuSpeed.duration && currentPosition - it.startTime > 0
            }
                .map {
                    if (it.parentWidth != 0) {
                        val speed =
                            (it.parentWidth + it.selfWidth) / danmakuSpeed.duration.toFloat()
                        val duration = currentPosition - it.startTime
                        val animationDuration = currentPosition - it.lastUpdateTime
                        val interval = (duration) * speed
//                        Logger.d("speed = $speed, duration = $duration ,interval = $interval,offset = ${it.parentWidth - interval}")
                        it.lastUpdateTime = currentPosition
                        it.duration = animationDuration
//                    val percent =
//                        (System.currentTimeMillis() - it.startTime) / danmakuSpeed.duration.toFloat()
                        val xOffset = it.parentWidth - interval
                        it.copy(offsetX = xOffset.toInt())
                    } else {
                        it
                    }
                }.toMutableList()

            targetList
                .filterIndexed { index, item ->
                    danmakuDensity == DanmakuDensity.Normal ||
                            index % danmakuDensity.code == 0
                }
                .forEach {

                    val trackIndex =
                        computeDanmakuTrackIndex(oldList, danmakuFontSize.textSize.value.toInt())

                    if (trackIndex != null) {
                        val item = DanmakuItem(
                            id = it.id,
                            content = it.content,
                            textSize = danmakuFontSize.textSize,
                            fontColor = it.rgb(colorful),
                            trackIndex = trackIndex,
                            offsetX = danmakuViewWidth
                        )

                        oldList.add(item)
                    }


                }

            oldList
        }
    }


    /**
     * 计算弹幕应该在第几条轨道上
     */
    private fun computeDanmakuTrackIndex(
        oldList: List<DanmakuItem>,
        danmakuItemHeight: Int = 72
    ): Int? {
        if (oldList.isEmpty()) {
            return 1
        }

        if (danmakuViewHeight == 0) {
            return Random(10).nextInt()
        }

        val count = danmakuViewHeight / danmakuItemHeight

        val usedTracks = oldList.map {
            it.trackIndex
        }.distinct()

        if (usedTracks.size < count - 1) {
            //还有可用的轨道 返回可用的轨道
            return List(count - 1) {
                it
            }.filter {
                !usedTracks.contains(it)
            }.random()
        }

        return oldList.groupBy {
            it.trackIndex
        }.map { map ->
            map.key to (map.value.maxOfOrNull {
                it.selfWidth + it.offsetX
            } ?: 0)

        }.filter {
            it.second < danmakuViewWidth
        }.map {
            it.first
        }.randomOrNull()
    }

    private val _bilibiliDanmakuList = mutableListOf<BilibiliDanmaku>()


    /**
     * 加载弹幕列表
     */
    private fun loadDanmakuList(id: Long) {
        viewModelScope.launch {
            _bilibiliDanmakuList.clear()
            _danmakuItemsForDisplay.value = emptyList()
            var index = 1
            while (true) {
                try {
                    val list = bilibiliRepository.danmakuList(1, id, index)
                    index++
                    if (list.isEmpty()) {
                        return@launch
                    }
                    _bilibiliDanmakuList.addAll(list)
                    delay(1000)
                } catch (e: Exception) {
                    CrashHandler.handler(e)
                    return@launch
                }
            }


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

                    reportHistory(currentPosition)

                    onVideoPositionChanged(currentPosition)
                }


                delay(1000)
            }
        }
    }

    private fun reportHistory(position: Long) {
        viewModelScope.launch {
            if (position != 0L) {
                bilibiliRepository.reportHistory(
                    params.aid,
                    params.cid,
                    position / 1000
                )
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


            try {
//                val view = bilibiliRepository.videoView(params.bvid).data!!

                val cid = params.cid
                val bvid = params.bvid

                initPlayer(cid, bvid)

            } catch (e: Exception) {
//                e.printStackTrace()
                CrashHandler.handler(e)
                _uiState.value = VideoDetailState.Error
            }
        }
    }

    /**
     * 初始化播放器
     */
    private suspend fun initPlayer(cid: Long, bvid: String) {
        _uiState.value = VideoDetailState.Loading

        val response = bilibiliRepository.videoUrl(cid, bvid).data!!
        loadDanmakuList(cid)
        response.dash.run {
            val videoList = video.map { dashVideo ->
                val text = VideoResolution.entries.first {
                    it.code == dashVideo.id
                }

                Resolution(text, dashVideo.baseUrl)
            }.distinctBy {
                it.videoResolution.code
            }.sortedByDescending { it.videoResolution.code }
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
                danmakuPosition = bilibiliStorage.danmakuPosition,
                playerViewShowMiniProgressBar = bilibiliStorage.playerViewShowMiniProgressBar
            ).apply {
                play(
                    currentVideoResolution.url,
                    audioList.first().url,
                    startPositionMs = if (abs(response.dash.duration - response.lastPlayTime / 1000) < 5) 0 else response.lastPlayTime
                )
            }

        }
    }

    override fun handleAction(action: VideoDetailAction) {

        when (action) {
            VideoDetailAction.Retry -> {
                if (currentBvid.isEmpty()) {
                    refresh()
                } else {
                    viewModelScope.launch {
                        loadParamsFromPlaylist(bvid = currentBvid)
                    }
                }

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

            VideoDetailAction.Forward -> {
                val target = player.currentPosition + 10000
                player.seekTo(if (target >= player.duration) player.duration else target)
            }

            is VideoDetailAction.UpdateSpeed -> {
                player.setPlaybackSpeed(action.videoSpeed.speed)
                _uiState.update {
                    (it as? VideoDetailState.Content)?.copy(videoSpeed = action.videoSpeed) ?: it
                }
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

data class DanmakuItem(
    val id: Long,
    /**
     * 弹幕内容
     */
    val content: String,
    /**
     * 弹幕字体大小
     */
    val textSize: TextUnit,
    /**
     * 弹幕颜色
     */
    val fontColor: Triple<Int, Int, Int>,
    /**
     * 自身宽度
     */
    val selfWidth: Int = 0,
    val parentWidth: Int = 0,
    val selfHeight: Int = 0,
    val parentHeight: Int = 0,
    /**
     * 开始显示时间
     */
    val startTime: Long = System.currentTimeMillis(),
//    val onShowTime: Long,
    val offsetX: Int = 0,
    val offsetY: Int = 0,
    /**
     * 弹幕在第几条轨道
     */
    val trackIndex: Int = 1,
    var lastUpdateTime: Long = 0,
    var duration: Long = 0,
//    val offsetYPercent: Float = Random.nextFloat()
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
        val playbackState: Int = Player.STATE_BUFFERING,
        val playerViewShowMiniProgressBar: Boolean = false,
        val videoSpeed: VideoSpeed = VideoSpeed.X1
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


    data object Forward : VideoDetailAction

    data class UpdateSpeed(val videoSpeed: VideoSpeed) : VideoDetailAction
}

sealed interface VideoDetailEvent {

    data class ShowVideoResolutionListDialog(
        val list: List<VideoResolution>,
        val current: VideoResolution
    ) : VideoDetailEvent

//    data class ShootDanmaku(val item: BilibiliDanmaku) : VideoDetailEvent

    data object BackToInfo : VideoDetailEvent
}