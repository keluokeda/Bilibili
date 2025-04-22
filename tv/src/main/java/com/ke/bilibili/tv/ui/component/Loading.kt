package com.ke.bilibili.tv.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ke.bilibili.tv.ui.theme.BilibiliPink


@Composable
fun Loading() {
    Text(
        "加载中",
        style = MaterialTheme.typography.titleLarge.copy(
            color = BilibiliPink
        ),
        modifier = Modifier
            .border(width = 1.dp, color = Color.White)
            .padding(16.dp)
    )
}