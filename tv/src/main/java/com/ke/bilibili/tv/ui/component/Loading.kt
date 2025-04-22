package com.ke.bilibili.tv.ui.component

import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ke.bilibili.tv.ui.theme.BilibiliPink


@Composable
fun Loading() {
    Text(
        "加载中",
        style = MaterialTheme.typography.headlineMedium.copy(
            color = BilibiliPink
        ),
//        modifier = Modifier
//            .border(width = 1.dp, color = Color.White)
//            .padding(16.dp)
    )
}