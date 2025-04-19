package com.ke.bilibili.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import com.ke.biliblli.common.Screen
import com.ke.biliblli.viewmodel.TvVideoInfoState
import com.ke.biliblli.viewmodel.TvVideoInfoViewModel
import kotlinx.coroutines.delay


@Composable
internal fun VideoInfoRoute(navigate: (Any) -> Unit) {
    val viewModel = hiltViewModel<TvVideoInfoViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                Text(stringResource(com.ke.bilibili.tv.R.string.loading))
            }

            is TvVideoInfoState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {

                    item {
                        val height = 300.dp * 9 / 16f

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height)
                        ) {
                            AsyncImage(
                                model = uiState.info.view.pic,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(height)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(24.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(
                                        height
                                    )
                            ) {
                                Text(
                                    uiState.info.view.title,
                                    style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
                                )
                                ListItem(selected = false, onClick = {
                                    val owner = uiState.info.view.owner
                                    navigate(
                                        Screen.UserDetail(
                                            owner.mid,
                                            owner.name,
                                            owner.face,
                                            ""
                                        )
                                    )
                                }, leadingContent = {
                                    AsyncImage(
                                        model = uiState.info.view.owner.face,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color.Gray),
                                        contentScale = ContentScale.Crop
                                    )
                                }, headlineContent = {

                                    Text(
                                        uiState.info.view.owner.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                )


                                Text(
                                    uiState.info.view.desc,
                                    style =
                                        MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            }
                        }
                    }

                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            items(uiState.info.tags) {
                                AssistChip(onClick = {}) {
                                    Text(it.name)
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val focusRequester = remember {
                                FocusRequester()
                            }

                            LaunchedEffect(Unit) {
                                delay(10)
                                focusRequester.requestFocus()
                            }

                            Button(onClick = {
                                navigate(
                                    Screen.VideoDetail(
                                        uiState.info.view.bvid,
                                        uiState.info.view.cid
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
                        }
                    }

                    if (uiState.info.view.ugcSeason != null) {
                        item {
                            Text(
                                "合集",
                                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        item {
                            LazyRow(
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
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }


                    item {
                        Text(
                            "推荐视频",
                            style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                            modifier = Modifier.padding(16.dp)
                        )
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
                                        it.title, maxLines = 1, modifier = Modifier.padding(8.dp)
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