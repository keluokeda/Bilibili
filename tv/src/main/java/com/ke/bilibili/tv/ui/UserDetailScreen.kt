package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.biliblli.api.response.SeasonsArchiveResponse
import com.ke.biliblli.api.response.UserResponse
import com.ke.biliblli.common.Screen
import com.ke.biliblli.viewmodel.UserDetailAction
import com.ke.biliblli.viewmodel.UserDetailItem
import com.ke.biliblli.viewmodel.UserDetailState
import com.ke.biliblli.viewmodel.UserDetailViewModel


@Composable
fun UserDetailRoute(navigate: (Any) -> Unit) {
    val viewModel = hiltViewModel<UserDetailViewModel>()

    val userVideos = viewModel.userVideos.collectAsLazyPagingItems()
    val userFans = viewModel.userFans.collectAsLazyPagingItems()
    val userFollowings = viewModel.userFollowings.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserDetailScreen(uiState, userVideos, navigate, userFans, userFollowings, {
        viewModel.handleAction(UserDetailAction.SelectItem(it))
    })
}

@Composable
private fun UserDetailScreen(
    uiState: UserDetailState,
    userVideos: LazyPagingItems<SeasonsArchiveResponse>,
    navigate: (Any) -> Unit,
    userFans: LazyPagingItems<UserResponse>,
    userFollowings: LazyPagingItems<UserResponse>,
    updateSection: (UserDetailItem) -> Unit
) {

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(320.dp)
                .padding(32.dp)
        ) {

            Card(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AsyncImage(
                        model = uiState.avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        uiState.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(uiState.sign, style = MaterialTheme.typography.labelMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            UserDetailItem.entries.forEach {
                ListItem(
                    selected = it == uiState.selected,
                    onClick = {
                        updateSection(it)
                    },
                    headlineContent = {
                        Text(it.displayName)
                    }
                )
            }

        }

        if (uiState.selected == UserDetailItem.Video) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(userVideos.itemCount) {
                    val item = userVideos[it]!!
                    UserVideo(item) {
                        navigate(Screen.VideoDetail(item.bvid))
                    }
                }
            }
        } else if (uiState.selected == UserDetailItem.Followers) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(userFans.itemCount) {
                    val item = userFans[it]!!
                    UserView(item) {
                        navigate(
                            Screen.UserDetail(
                                item.mid,
                                name = item.uname,
                                avatar = item.face,
                                sign = item.sign
                            )
                        )
                    }
                }
            }
        } else if (uiState.selected == UserDetailItem.Following) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(userFollowings.itemCount) {
                    val item = userFollowings[it]!!
                    UserView(item) {
                        navigate(
                            Screen.UserDetail(
                                item.mid,
                                name = item.uname,
                                avatar = item.face,
                                sign = item.sign
                            )
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun UserView(userResponse: UserResponse, onClick: () -> Unit) {
    Card(
        onClick, modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = userResponse.face,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(userResponse.uname, modifier = Modifier.padding(top = 16.dp), maxLines = 1)
        }
    }
}

@Composable
private fun UserVideo(item: SeasonsArchiveResponse, onClick: () -> Unit) {
    Card(onClick, modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = item.pic,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f),
            contentScale = ContentScale.Crop
        )

        Text(item.title + "\n", maxLines = 2, modifier = Modifier.padding(8.dp))
    }
}