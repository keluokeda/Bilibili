package com.ke.bilibili.tv.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ke.bilibili.tv.ui.theme.BilibiliPink
import com.ke.bilibili.tv.ui.theme.BilibiliTheme

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    max: Long,
    current: Long,
    height: Dp = 4.dp,
) {

    val value = current / max.toFloat()

    val progress = if (value >= 0f && value <= 1f) value else 1f




    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(4.dp))
    ) {

        Row(
            modifier
                .fillMaxWidth()
                .height(height)
                .background(color = Color.White.copy(alpha = 0.3f))

        ) {

            if (progress != 0f)
                Box(
                    modifier = Modifier
                        .weight(progress)
                        .fillMaxHeight()
                        .background(color = BilibiliPink)
                )
            if (progress != 1f)
                Spacer(modifier = Modifier.weight(1 - progress))
        }
    }
}

@Preview
@Composable
private fun ProgressBarPreview() {
    BilibiliTheme {
        ProgressBar(Modifier, 100L, 50L, 8.dp)
    }
}