package com.ke.bilibili.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.biliblli.common.duration
import com.ke.biliblli.common.entity.VideoViewEntity
import com.ke.biliblli.common.format




@Composable
fun VideoItem(item: VideoViewEntity, onClick: () -> Unit = {}) {

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
                    item.view.format(),
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
                    item.danmaku.format(),
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
            style = MaterialTheme.typography.labelMedium,
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
                modifier = Modifier.size(12.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                item.author,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray
                ),
                maxLines = 1
            )

        }
        Spacer(modifier = Modifier.height(8.dp))

    }
}