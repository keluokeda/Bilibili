package com.ke.biliblli.mobile.ui.home.recommend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ke.biliblli.api.response.HomeRecommendResponse
import com.ke.biliblli.api.response.VideoOwner
import com.ke.biliblli.api.response.VideoStatus
import com.ke.biliblli.common.duration
import com.ke.biliblli.common.format
import com.ke.biliblli.mobile.ui.theme.BilibiliTheme
import com.ke.biliblli.viewmodel.HomeRecommendViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRecommendVideoListRoute(
    onClick: (HomeRecommendResponse) -> Unit
) {

    val viewModel = hiltViewModel<HomeRecommendViewModel>()
    val lazyPagingItems = viewModel.videoListFlow.collectAsLazyPagingItems()

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = lazyPagingItems.loadState.refresh == LoadState.Loading,
        onRefresh = {
            lazyPagingItems.refresh()
        }) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            items(count = lazyPagingItems.itemCount) {
                val item = lazyPagingItems[it]!!


                VideoItem(item, onClick = {
                    onClick(item)
                })
            }


        }
    }
}

@Composable
private fun VideoItem(item: HomeRecommendResponse, onClick: () -> Unit = {}) {

    Card(onClick = onClick) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9.0f)
        ) {

            AsyncImage(
                model = item.pic,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()

            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .align(alignment = Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.PlayCircleOutline,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    item.stat.view.format(),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )

                Spacer(modifier = Modifier.width(8.dp))


                Icon(
                    Icons.AutoMirrored.Filled.Comment,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    item.stat.danmaku.format(),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    item.duration.duration(),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )

            }
        }

        Text(
            item.title + "\n",
            maxLines = 2,
            modifier = Modifier.padding(4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.AccountCircle,
                null,
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                item.owner.name,
                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                maxLines = 1
            )

        }
        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
@PreviewLightDark
private fun VideoItemPreview() {
    BilibiliTheme {
        VideoItem(
            HomeRecommendResponse(
                id = 0,
                bvid = "",
                cid = 0,
                goto = "",
                uri = "",
                pic = "https://i2.hdslb.com/bfs/archive/4968b1841933d4efce3026aabd5d2e0346a08703.jpg",
                title = "【不良人7】第3集预告解读：降臣夜会多阔霍，李星云再受伤？",
                duration = 397,
                pubdate = 1743597574,
                owner = VideoOwner(
                    mid = 0,
                    name = "贝贝漫",
                    face = "https://i1.hdslb.com/bfs/face/c2bbf64f8d302f7c5f2fcc3bb1a7ee684bad41fc.jpg"
                ),
                stat = VideoStatus(view = 218142, like = 8039, danmaku = 374, vt = 0)
            )
        )
    }
}