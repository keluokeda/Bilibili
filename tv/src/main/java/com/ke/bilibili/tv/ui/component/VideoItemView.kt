package com.ke.bilibili.tv.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ClassicCard
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.bilibili.tv.ui.theme.BilibiliTheme

data class VideoItem(
    val title: String,
    val image: String,
    val view: String,
    val danmaku: String,
    val duration: String,
    val lastProgress: String,
    val author: String
)

@Composable
fun VideoItemView(videoItem: VideoItem, onClick: () -> Unit) {
    ClassicCard(onClick = onClick, image = {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .background(color = Color.Gray)
        ) {
            AsyncImage(
                model = videoItem.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.PlayCircleOutline,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    videoItem.view,
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
                    videoItem.danmaku,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    if (videoItem.lastProgress.isEmpty()) {
                        videoItem.duration
                    } else {
                        videoItem.lastProgress + "/" + videoItem.duration
                    },
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
            }
        }

    }, title = {
        Text(
            videoItem.title + "\n",
            maxLines = 2,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
        )
    }, subtitle = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 2.dp, bottom = 2.dp)
        ) {
            Icon(Icons.Default.AccountCircle, null, tint = Color.Gray)
            Text(videoItem.author, maxLines = 1)
        }
    })
}

@Preview
@Composable
private fun VideoItemViewPreview() {
    BilibiliTheme {
        val item = VideoItem(
            "骁龙8s Gen4前瞻上手：次旗舰平台更新啦！",
            image = "",
            view = "1195011",
            danmaku = "1905",
            duration = "12:00",
            lastProgress = "9:30",
            author = "汉库克"
        )

        VideoItemView(item) { }
    }
}