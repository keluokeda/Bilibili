package com.ke.bilibili.tv.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.AssistChip
import androidx.tv.material3.Button
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.bilibili.tv.ui.component.Loading
import com.ke.bilibili.tv.ui.theme.BilibiliPink
import com.ke.biliblli.common.Screen
import com.ke.biliblli.common.duration
import com.ke.biliblli.viewmodel.TvVideoInfoState
import com.ke.biliblli.viewmodel.TvVideoInfoViewModel


@Composable
internal fun VideoInfoRoute(navigate: (Any) -> Unit) {
    val viewModel = hiltViewModel<TvVideoInfoViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.observeWithLifecycle {
        navigate(it.detail)
    }

    VideoInfoScreen(uiState, {
        viewModel.handleAction(Unit)
    }, navigate)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun VideoInfoScreen(uiState: TvVideoInfoState, retry: () -> Unit, navigate: (Any) -> Unit) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (uiState) {
            TvVideoInfoState.Error -> {
                Button(retry) {
                    Text("出错了，点我重试")
                }
            }

            TvVideoInfoState.Loading -> {
                Loading()
            }

            is TvVideoInfoState.Success -> {

                var backgroundImage by remember {
                    mutableStateOf(uiState.info.view.pic)
                }

                Box(modifier = Modifier.fillMaxSize()) {

                    Crossfade(backgroundImage) {
                        AsyncImage(
                            model = it,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }




                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black.copy(alpha = .7f))
                            .padding(
                                24.dp
                            )
                    ) {

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    uiState.info.view.title,
                                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                                )

                                if (uiState.info.view.desc.isNotEmpty() && uiState.info.view.desc != "-") {
                                    Text(
                                        uiState.info.view.desc,
                                        maxLines = 2,
                                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                                    )
                                }


                            }
                        }

//                        item {
//                            val height = 300.dp * 9 / 16f
//
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(height)
//                            ) {
//                                AsyncImage(
//                                    model = uiState.info.view.pic,
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .width(300.dp)
//                                        .height(height)
//                                        .clip(RoundedCornerShape(8.dp))
//                                )
//                                Spacer(modifier = Modifier.width(24.dp))
//
//                                Column(
//                                    modifier = Modifier
//                                        .weight(1f)
//                                        .height(
//                                            height
//                                        )
//                                ) {
//                                    Text(
//                                        uiState.info.view.title,
//                                        style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
//                                    )
//                                    ListItem(selected = false, onClick = {
//                                        val owner = uiState.info.view.owner
//                                        navigate(
//                                            Screen.UserDetail(
//                                                owner.mid,
//                                                owner.name,
//                                                owner.face,
//                                                ""
//                                            )
//                                        )
//                                    }, leadingContent = {
//                                        AsyncImage(
//                                            model = uiState.info.view.owner.face,
//                                            contentDescription = null,
//                                            modifier = Modifier
//                                                .size(40.dp)
//                                                .clip(CircleShape)
//                                                .background(Color.Gray),
//                                            contentScale = ContentScale.Crop
//                                        )
//                                    }, headlineContent = {
//
//                                        Text(
//                                            uiState.info.view.owner.name,
//                                            style = MaterialTheme.typography.titleMedium
//                                        )
//                                    }
//                                    )
//
//
//                                    Text(
//                                        uiState.info.view.desc,
//                                        style =
//                                            MaterialTheme.typography.bodyLarge.copy(color = Color.White),
//                                        modifier = Modifier
//                                            .weight(1f)
//                                    )
//                                }
//                            }
//                        }


                        item {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .onFocusChanged {
                                        if (it.hasFocus) {
                                            backgroundImage = uiState.info.view.pic
                                        }
                                    },
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                val focusRequester = remember {
                                    FocusRequester()
                                }
                                Button(onClick = {
                                    navigate(
                                        Screen.VideoDetail(
                                            uiState.info.view.bvid,
                                            uiState.info.view.cid,
                                            uiState.info.view.aid
                                        )
                                    )
                                }, modifier = Modifier.focusRequester(focusRequester)) {
                                    Icon(Icons.Default.PlayCircle, null)
                                    Text("播放")
                                }

                                Button(onClick = {
                                    navigate(Screen.Comment(uiState.info.view.aid, 1))
                                }) {
                                    Icon(Icons.AutoMirrored.Filled.Comment, null)
                                    Text("评论")
                                }

                                Button(onClick = {}, enabled = false) {
                                    Icon(Icons.Default.History, null)
                                    Text(uiState.info.view.duration.duration())
                                }

                                Button(onClick = {}, enabled = false) {
                                    Icon(Icons.Default.CalendarMonth, null)
                                    Text(uiState.info.view.timeText())
                                }
                            }
                        }

                        item {
                            val owner = uiState.info.view.owner
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {


                                AssistChip(onClick = {
                                    navigate(
                                        Screen.UserDetail(
                                            owner.mid,
                                            owner.name,
                                            owner.face,
                                            ""
                                        )
                                    )
                                }, leadingIcon = {
                                    AsyncImage(
                                        model = uiState.info.view.owner.face,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(color = Color.Gray)
                                    )
                                }, modifier = Modifier.onFocusChanged {
                                    if (it.hasFocus) {
                                        backgroundImage = uiState.info.view.pic
                                    }
                                }) {
                                    Text(owner.name)
                                }
                            }
//                            ListItem(
//                                selected = false, onClick = {
//                                    val owner = uiState.info.view.owner
//                                    navigate(
//                                        Screen.UserDetail(
//                                            owner.mid,
//                                            owner.name,
//                                            owner.face,
//                                            ""
//                                        )
//                                    )
//                                }, headlineContent = {
//                                    Text(uiState.info.view.owner.name)
//                                }, leadingContent = {
//                                    AsyncImage(
//                                        model = uiState.info.view.owner.face,
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .size(40.dp)
//                                            .clip(CircleShape)
//                                            .background(color = Color.Gray)
//                                    )
//                                }, modifier = Modifier.onFocusChanged {
//                                    if (it.hasFocus) {
//                                        backgroundImage = uiState.info.view.pic
//                                    }
//                                }
//                            )
                        }


                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .padding(16.dp)
//                                    .padding(horizontal = 8.dp)
                                    .onFocusChanged {
                                        if (it.hasFocus) {
                                            backgroundImage = uiState.info.view.pic
                                        }
                                    }
                            ) {
                                items(uiState.info.tags) {
                                    AssistChip(onClick = {
                                        navigate(Screen.Search(it.name))
                                    }) {
                                        Text(it.name)
                                    }
                                }
                            }
                        }


                        if (uiState.pageList.size > 1) {
                            item {
                                ListItem(selected = false, onClick = {
//                                    val list = uiState.info.view.ugcSeason!!.allVideos()
//                                    val playlist = mutableListOf<String>()
//                                    var foundTarget = false
//                                    list.forEach {
//                                        if (it.cid == uiState.info.view.cid) {
////                                            playlist.add()
//                                            foundTarget = true
//                                        } else if (foundTarget) {
//                                            playlist.add(it.bvid)
//                                        }
//                                    }
//
//                                    navigate(Screen.VideoDetail(bvid = uiState.info.view.bvid, cid = uiState.info.view.cid, aid = uiState.info.view.aid,playlist))
                                }, headlineContent = {
                                    Text("视频选集")
                                }, trailingContent = {
//                                    Text("播放全部")
                                })
                            }



                            item {
                                val lazyListState = remember { LazyListState() }

                                LaunchedEffect(Unit) {
                                    val index = uiState.pageList.indexOfFirst {
                                        it.cid == uiState.info.view.cid
                                    }
                                    if (index != -1) {
                                        lazyListState.scrollToItem(index)
                                    }
                                }

                                LazyRow(
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    state = lazyListState
                                ) {
                                    items(uiState.pageList) {
                                        Card(
                                            onClick = {
//                                            navigate(Screen.VideoInfo(it.bvid))
                                                val cidList = mutableListOf<Long>()
                                                var found = false
                                                uiState.pageList.forEach { page ->
                                                    if (found) {
                                                        cidList.add(page.cid)
                                                    } else if (page.cid == it.cid) {
                                                        found = true
                                                    }
                                                }

                                                navigate(
                                                    Screen.VideoDetail(
                                                        uiState.info.view.bvid,
                                                        it.cid,
                                                        uiState.info.view.aid, cidList = cidList
                                                    )
                                                )
                                            },
                                            modifier = Modifier
                                                .width(240.dp)
                                                .onFocusChanged { focusState ->
                                                    if (focusState.isFocused && it.firstFrame != null) {
                                                        backgroundImage = it.firstFrame!!
                                                    }
                                                }
                                        ) {

                                            if (it.firstFrame != null) {
                                                AsyncImage(
                                                    model = it.firstFrame,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .aspectRatio(16 / 9f),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                            Text(
                                                it.part,
                                                maxLines = 1,
                                                modifier = Modifier.padding(8.dp),
                                                style = TextStyle(color = if (it.cid == uiState.info.view.cid) BilibiliPink else Color.White)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (uiState.info.view.ugcSeason != null) {
                            item {
                                ListItem(selected = false, onClick = {
                                    val list = uiState.info.view.ugcSeason!!.allVideos()
                                    val playlist = mutableListOf<String>()
                                    var foundTarget = false
                                    list.forEach {
                                        if (it.cid == uiState.info.view.cid) {
//                                            playlist.add()
                                            foundTarget = true
                                        } else if (foundTarget) {
                                            playlist.add(it.bvid)
                                        }
                                    }

                                    navigate(
                                        Screen.VideoDetail(
                                            bvid = uiState.info.view.bvid,
                                            cid = uiState.info.view.cid,
                                            aid = uiState.info.view.aid,
                                            playlist
                                        )
                                    )
                                }, headlineContent = {
                                    Text("合集")
                                }, trailingContent = {
                                    Text("从当前位置播放全部")
                                })
                            }

                            item {


                                val lazyListState = remember { LazyListState() }

                                LaunchedEffect(Unit) {
                                    val index =
                                        uiState.info.view.ugcSeason!!.allVideos().indexOfFirst {
                                            it.cid == uiState.info.view.cid
                                        }
                                    if (index != -1) {
                                        lazyListState.scrollToItem(index)
                                    }
                                }

                                LazyRow(
                                    state = lazyListState,
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(uiState.info.view.ugcSeason!!.allVideos()) {
                                        Card(
                                            onClick = {
                                                navigate(Screen.VideoInfo(it.bvid))
                                            },
                                            modifier = Modifier
                                                .width(240.dp)
                                                .onFocusChanged { focusState ->
                                                    if (focusState.isFocused) {
                                                        backgroundImage = it.arc.pic
                                                    }
                                                }
                                        ) {
                                            AsyncImage(
                                                model = it.arc.pic,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(16 / 9f),
                                                contentScale = ContentScale.Crop
                                            )
                                            Text(
                                                it.title,
                                                maxLines = 1,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    color = if (uiState.info.view.cid == it.cid) BilibiliPink else Color.White
                                                ),
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }


                        item {
//                            Text(
//                                "推荐视频",
//                                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
//                                modifier = Modifier.padding(16.dp)
//                            )
                            ListItem(selected = false, onClick = {
                                val list = uiState.info.related.map {
                                    Screen.VideoDetail(it.bvid, it.cid, it.aid)
                                }.toMutableList()
                                val target = list.removeAt(0)

                                navigate(
                                    target.copy(
                                    playlist = list.map { it.bvid }
                                ))
                            }, headlineContent = {
                                Text("推荐视频")
                            }, trailingContent = {
                                Text("播放全部")
                            })
                        }

                        item {
                            LazyRow(
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(uiState.info.related) {
                                    Card(
                                        onClick = {
                                            navigate(Screen.VideoInfo(it.bvid))
                                        },
                                        modifier = Modifier
                                            .width(240.dp)
                                            .onFocusChanged { focusState ->
                                                if (focusState.isFocused) {
                                                    backgroundImage = it.pic
                                                }
                                            }
                                    ) {
                                        AsyncImage(
                                            model = it.pic,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(16 / 9f),
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            it.title,
                                            maxLines = 1,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }


                }

            }
        }


    }


}