@file:OptIn(ExperimentalMaterial3Api::class)

package com.ke.biliblli.mobile.ui.video

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.ke.biliblli.api.response.VideoDetailResponse
import com.ke.biliblli.common.Screen
import com.ke.biliblli.common.format
import com.ke.biliblli.db.entity.CommentEntity
import com.ke.biliblli.mobile.observeWithLifecycle
import com.ke.biliblli.mobile.ui.theme.BilibiliTheme
import com.ke.biliblli.viewmodel.VideoCommentSortType
import com.ke.biliblli.viewmodel.VideoCommentsEvent
import com.ke.biliblli.viewmodel.VideoCommentsViewModel
import com.ke.biliblli.viewmodel.VideoDetailAction
import com.ke.biliblli.viewmodel.VideoDetailEvent
import com.ke.biliblli.viewmodel.VideoDetailState
import com.ke.biliblli.viewmodel.VideoDetailViewModel
import com.ke.biliblli.viewmodel.VideoInfoAction
import com.ke.biliblli.viewmodel.VideoInfoState
import com.ke.biliblli.viewmodel.VideoInfoViewModel
import com.ke.biliblli.viewmodel.VideoResolution
import com.orhanobut.logger.Logger
import dev.vivvvek.seeker.Seeker
import dev.vivvvek.seeker.SeekerDefaults
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


private val tabs = listOf("简介", "评论")

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailRoute(
    toVideoDetail: (Screen.VideoDetail) -> Unit
) {

    val viewModel = hiltViewModel<VideoDetailViewModel>()


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()






    VideoDetailScreen(uiState, {
        viewModel.handleAction(VideoDetailAction.Retry)
    }, {
        viewModel.handleAction(VideoDetailAction.SetFullScreen(it))
    }, {
        viewModel.handleAction(VideoDetailAction.ShowController(it))
    }, {
        viewModel.handleAction(VideoDetailAction.OnSliderValueChangeFinished(it))
    }, {
        viewModel.handleAction(VideoDetailAction.PlayPauseVideo(it))
    }, toVideoDetail = toVideoDetail, {
        viewModel.handleAction(VideoDetailAction.ShowVideoResolutionListDialog)
    })


    var videoResolutionPair by rememberSaveable {
        mutableStateOf<Pair<List<VideoResolution>, VideoResolution>?>(null)
    }

    viewModel.event.observeWithLifecycle {
        when (it) {
            is VideoDetailEvent.ShowVideoResolutionListDialog -> {
                videoResolutionPair = it.list to it.current
            }
        }
    }

    if (videoResolutionPair != null) {
        ModalBottomSheet(onDismissRequest = {
            videoResolutionPair = null
        }) {
            val pair = videoResolutionPair!!

            pair.first.forEach { videoResolution ->
                ListItem(headlineContent = {
                    Text(videoResolution.text)
                }, leadingContent = {
                    if (videoResolution == pair.second) {
                        Icon(Icons.Default.CheckBox, null, tint = MaterialTheme.colorScheme.primary)
                    } else {
                        Icon(Icons.Default.CheckBoxOutlineBlank, null)
                    }
                }, modifier = Modifier.clickable(enabled = videoResolution != pair.second) {
                    viewModel.handleAction(VideoDetailAction.UpdateVideoResolution(videoResolution))
                    videoResolutionPair = null
                })
            }
        }
    }
}

@UnstableApi
@Composable
private fun VideoDetailScreen(
    uiState: VideoDetailState,
    retry: () -> Unit,
    setFullScreen: (Boolean) -> Unit,
    showController: (Boolean) -> Unit,
    onValueChangeFinished: (Long) -> Unit,
    setPlay: (Boolean) -> Unit,
    toVideoDetail: (Screen.VideoDetail) -> Unit,
    showVideoResolutionListDialog: () -> Unit
) {


    Scaffold(
        topBar = {
            if (uiState !is VideoDetailState.Content) {
                TopAppBar(
                    title = {
                        Text("视频详情")
                    }
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is VideoDetailState.Content -> {

                    BackHandler(uiState.isFullScreen) {
                        setFullScreen(false)
                    }


                    val context = LocalContext.current


                    LaunchedEffect(uiState.isFullScreen) {
//                        exoPlayer.seekTo(uiState.currentPosition)
                        if (uiState.isFullScreen) {
                            context.setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                        } else {
                            context.setScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        }
                    }


                    val modifier = if (uiState.isFullScreen) {
                        Modifier.fillMaxSize()
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(uiState.ratio)
                    }



                    Column(modifier = Modifier.fillMaxSize()) {

                        Box(modifier) {


                            AndroidView(
                                factory = { context ->
                                    PlayerView(context).apply {
                                        player = uiState.player
                                        useController = false
                                    }

                                }, modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(enabled = !uiState.showController) {
                                        showController(true)
//                                        toVideoDetail("", 0)
                                    }
                            )

                            if (uiState.playbackState == Player.STATE_BUFFERING) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            if (uiState.showController) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = Color.Black.copy(
                                                alpha = 0.5f
                                            )
                                        )

                                        .clickable {
                                            showController(false)
                                        }
                                        .padding(8.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        val dispatcher = LocalOnBackPressedDispatcherOwner.current

                                        IconButton(onClick = {
                                            dispatcher?.onBackPressedDispatcher?.onBackPressed()
                                        }) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack, null
                                            )
                                        }

                                        IconButton(onClick = {}) {
                                            Icon(Icons.Default.Home, null)
                                        }

                                        Spacer(modifier = Modifier.weight(1f))

                                        TextButton(onClick = showVideoResolutionListDialog) {
                                            Text(uiState.currentVideoResolution.text)
                                        }


//                                        IconButton(onClick = showVideoResolutionListDialog) {
//                                            Icon(Icons.Default.MoreVert, null)
//                                        }
                                    }

                                    Spacer(modifier = Modifier.weight(1f))



                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = {
                                            setPlay(!uiState.isPlaying)
                                        }) {
                                            Icon(
                                                if (uiState.isPlaying) Icons.Default.Pause else
                                                    Icons.Default.PlayArrow, null
                                            )
                                        }

                                        var progress by remember {
                                            mutableFloatStateOf(-1f)
                                        }

//                                        Logger.d("duration = ${uiState.player.duration}")

                                        val duration =
                                            if (uiState.player.duration < 0) 0 else uiState.player.duration

                                        Seeker(
                                            value = if (progress == -1f) uiState.currentPosition.toFloat() else progress,
                                            range = 0f..duration.toFloat(),
                                            readAheadValue = uiState.player.bufferedPosition.toFloat(),
                                            onValueChange = {
                                                progress = it
                                            },
                                            onValueChangeFinished = {
                                                onValueChangeFinished(progress.toLong())
                                            },
                                            colors = SeekerDefaults.seekerColors(

                                            ),
                                            modifier = Modifier.weight(1f)
                                        )



                                        IconButton(onClick = {
                                            setFullScreen(!uiState.isFullScreen)
                                        }) {
                                            Icon(
                                                if (uiState.isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                                null
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (!uiState.isFullScreen) {

//                            VideoInfoRoute(toVideoDetail)
//                            VideoCommentsRoute()
//                            var selectedIndex by remember {
//                                mutableIntStateOf(0)
//                            }
                            Column(modifier = Modifier.fillMaxSize()) {

                                val pagerState = rememberPagerState(pageCount = {
                                    tabs.size
                                })
                                val scope = rememberCoroutineScope()

                                PrimaryTabRow(
                                    selectedTabIndex = pagerState.currentPage,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    tabs.forEachIndexed { index, title ->
                                        Tab(selected = index == pagerState.currentPage, onClick = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }, text = {
                                            Text(title)
                                        })
                                    }
                                }


                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ) {
                                    if (it == 0) {
                                        VideoInfoRoute {
                                            toVideoDetail(it)
                                        }
                                    } else {
                                        VideoCommentsRoute()
                                    }
                                }
                            }


                        }
                    }


                }

                VideoDetailState.Error -> {
                    OutlinedButton(onClick = retry) {
                        Text("出错了，点我重试")
                    }
                }

                VideoDetailState.Loading -> {
                    CircularProgressIndicator()
                }
            }


        }

    }
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.setScreenOrientation(orientation: Int) {
    val activity = this.findActivity() ?: return
    activity.requestedOrientation = orientation
    if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        hideSystemUi()
    } else {
        showSystemUi()
    }
}

private fun Context.hideSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

private fun Context.showSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}

@Composable
@PreviewLightDark
private fun SliderPreview() {
    BilibiliTheme {
        val interactionSource = remember {
            MutableInteractionSource()
        }
        Slider(
            value = 0.3f,
            onValueChange = {

            },
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(interactionSource, thumbSize = DpSize(16.dp, 16.dp))
            },
        )
    }

}

@Composable
@PreviewLightDark
private fun SeekerPreview() {

    BilibiliTheme {
        Seeker(
            value = 0.3f, onValueChange = {

            }, readAheadValue = 0.4f, colors = SeekerDefaults.seekerColors(
                readAheadColor = Color.Gray
            )
        )
    }
}

@Composable
private fun VideoInfoRoute(
    toVideoDetail: (Screen.VideoDetail) -> Unit
) {
    val viewModel = hiltViewModel<VideoInfoViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    VideoInfoScreen(uiState, retry = {
        viewModel.handleAction(VideoInfoAction.Retry)
    }, setExpanded = {
        viewModel.handleAction(VideoInfoAction.SetExpanded(it))
    }, toVideoDetail = toVideoDetail)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VideoInfoScreen(
    uiState: VideoInfoState,
    retry: () -> Unit,
    setExpanded: (Boolean) -> Unit,
    toVideoDetail: (Screen.VideoDetail) -> Unit
) {


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {
            VideoInfoState.Error -> {
                OutlinedButton(retry) {
                    Text("出错了，点我重试")
                }
            }

            VideoInfoState.Loading -> {
                CircularProgressIndicator()
            }

            is VideoInfoState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)

                ) {

                    item {
                        val detail = uiState.info.view
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
//                                .padding(16.dp)
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(detail.owner.name, maxLines = 1)
                                }, leadingContent = {
                                    AsyncImage(
                                        model = detail.owner.face,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                    )
                                }, supportingContent = {
                                    Text(
                                        uiState.info.card.card.sign,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }, trailingContent = {
                                    val buttonModifier = Modifier
                                        .width(80.dp)
                                        .height(32.dp)
                                    if (uiState.info.card.following) {
                                        TextButton(onClick = {}, modifier = buttonModifier) {
                                            Text(
                                                "已关注",
                                                modifier = buttonModifier,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        TextButton(onClick = {}, modifier = buttonModifier) {
                                            Text(
                                                "+关注",
                                                modifier = buttonModifier,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .clickable(onClick = {
                                        setExpanded(!uiState.expanded)
                                    })
                            ) {
                                Text(
                                    uiState.info.view.title,
                                    maxLines = if (uiState.expanded) Int.MAX_VALUE else 1,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    if (uiState.expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val textStyle =
                                    MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
                                Icon(
                                    Icons.Default.PlayCircleOutline,
                                    null,
                                    modifier = Modifier.size(textStyle.fontSize.value.dp),
                                    tint = textStyle.color
                                )
                                Text(
                                    uiState.info.view.stat.view.format(),
                                    style = textStyle
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(
                                    Icons.AutoMirrored.Filled.Comment,
                                    null,
                                    modifier = Modifier.size(textStyle.fontSize.value.dp),
                                    tint = textStyle.color
                                )
                                Text(
                                    uiState.info.view.stat.danmaku.format(),
                                    style = textStyle
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    simpleDateFormat.format(Date(uiState.info.view.ctime * 1000)),
                                    style = textStyle
                                )

                            }

                            if (uiState.expanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()

                                ) {

                                    Text(
                                        uiState.info.view.desc,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(all = 16.dp)
                                    )
                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        uiState.info.tags.forEach {
                                            AssistChip(
                                                onClick = {},
                                                label = {
                                                    Text(it.name)
                                                }
                                            )
                                        }
                                    }


                                }

                            } else {
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                VerticalIconButton(
                                    Icons.Default.ThumbUp,
                                    color = Color.Gray,
                                    text = uiState.info.view.stat.like.format()
                                ) { }


                                VerticalIconButton(
                                    Icons.Default.CurrencyBitcoin,
                                    color = Color.Gray,
                                    text = uiState.info.view.stat.coin.format()
                                ) { }

                                VerticalIconButton(
                                    Icons.Default.Star,
                                    color = Color.Gray,
                                    text = uiState.info.view.stat.favorite.format()
                                ) { }

                                VerticalIconButton(
                                    Icons.Default.Share,
                                    color = Color.Gray,
                                    text = uiState.info.view.stat.share.format()
                                ) { }
                            }
                        }
                    }


                    items(uiState.info.related) {
                        RelatedVideoItem(it) {
                            toVideoDetail(Screen.VideoDetail(it.cid, it.bvid, it.aid))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VerticalIconButton(
    imageVector: ImageVector,
    color: Color,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector, null, tint = color)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, style = MaterialTheme.typography.labelMedium.copy(color = color))
    }
}

@Composable
@PreviewLightDark
private fun VerticalIconButtonPreview() {
    BilibiliTheme {
        VerticalIconButton(Icons.Default.ThumbUp, color = Color.Gray, "10") { }
    }
}


private val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm")

@Composable
private fun RelatedVideoItem(item: VideoDetailResponse, onClick: () -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onClick()
            })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            AsyncImage(
                model = item.pic,
                contentDescription = null,
                modifier = Modifier
                    .width(160.dp)
                    .height(90.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))

            val color = Color.Gray
            val size = 13.dp

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(item.title + "\n", maxLines = 2, style = MaterialTheme.typography.bodySmall)
                Row(modifier = Modifier.padding(vertical = 3.dp)) {
                    Icon(
                        Icons.Default.AccountCircle,
                        modifier = Modifier.size(size),
                        contentDescription = null,
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(item.owner.name, style = TextStyle(fontSize = 12.sp, color = color))
                }

                Row {
                    Icon(
                        Icons.Default.PlayCircleOutline,
                        modifier = Modifier.size(size),
                        contentDescription = null,
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        item.stat.view.format(),
                        style = TextStyle(fontSize = 12.sp, color = color)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        Icons.AutoMirrored.Filled.Comment,
                        modifier = Modifier.size(size),
                        contentDescription = null,
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        item.stat.danmaku.format(),
                        style = TextStyle(fontSize = 12.sp, color = color)
                    )
                }
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Composable
private fun VideoCommentsRoute() {
    val viewModel = hiltViewModel<VideoCommentsViewModel>()
    val comments = viewModel.comments.collectAsLazyPagingItems()

    val sort by viewModel.sort.collectAsStateWithLifecycle()

    viewModel.event.observeWithLifecycle {
        when (it) {
            VideoCommentsEvent.Refresh -> {
                comments.refresh()
            }
        }
    }

    VideoCommentScreen(comments, sort, {
        viewModel.toggleSort()
    })
}

@Composable
private fun VideoCommentScreen(
    comments: LazyPagingItems<CommentEntity>,
    sort: VideoCommentSortType,
    toggleSort: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .clickable(onClick = toggleSort)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(sort.leading)

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                Icons.AutoMirrored.Filled.List,
                null,
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
            Text(sort.training, style = TextStyle(color = Color.Gray, fontSize = 12.sp))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(comments.itemCount, key = comments.itemKey(key = {
                it.id
            }), contentType = comments.itemContentType()) {
                CommentView(comments[it]!!)
            }
        }


    }


}

@Composable
fun CommentView(comment: CommentEntity) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            AsyncImage(
                model = comment.avatar,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(comment.username, style = MaterialTheme.typography.labelMedium)
                Text(
                    "${comment.timeDesc} ${comment.ip}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(comment.content, style = MaterialTheme.typography.labelLarge)

                Row {
                    TextButton(onClick = {

                    }) {
                        Icon(Icons.Default.ThumbUpOffAlt, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            comment.like.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )


                    }

                    Spacer(modifier = Modifier.weight(1f))

                    var expanded by remember {
                        mutableStateOf(false)
                    }

                    Box(
                        modifier = Modifier
                    ) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More options",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("评论") },
                                onClick = { /* Do something... */ },
                                leadingIcon = {
                                    Icon(Icons.AutoMirrored.Filled.Comment, null)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("复制") },
                                onClick = { /* Do something... */ },
                                leadingIcon = {
                                    Icon(Icons.Default.CopyAll, null)
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("举报")
                                },
                                onClick = {

                                },
                                leadingIcon = {
                                    Icon(Icons.Default.WarningAmber, null)
                                },
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("加入黑名单")
                                }, onClick = {

                                }, leadingIcon = {
                                    Icon(Icons.Default.Block, null)
                                }
                            )
                        }
                    }

                }
            }
        }

        HorizontalDivider(thickness = 1.dp)

    }
}