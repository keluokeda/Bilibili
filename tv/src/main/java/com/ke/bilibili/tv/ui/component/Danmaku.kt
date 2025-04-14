package com.ke.bilibili.tv.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import kotlinx.serialization.Serializable
import kotlin.random.Random


@Composable
fun DanmakuView(
    modifier: Modifier,
    sender: ((DanmakuItem) -> Unit) -> Unit
) {

    var danmakuItemList by remember {
        mutableStateOf<List<DanmakuItem>>(emptyList())
    }

    LaunchedEffect(sender) {
        sender {
            danmakuItemList += it
        }

    }




    BoxWithConstraints(modifier = modifier) {
        this.constraints.maxHeight

        danmakuItemList.forEach { item ->


            var danmakuItem by remember(item.id) {
                mutableStateOf<DanmakuItem>(item)
            }

            var move by remember(danmakuItem.id) { mutableStateOf(false) }


            val percent by animateIntAsState(
                targetValue = if (move) 0 else 100, animationSpec = tween(
                    durationMillis = danmakuItem.duration.toInt(),
                    easing = LinearEasing
                ), label = danmakuItem.id
            )



            if (percent == 0) {
                item.percent = 0
            }



            if (item.percent != 0) {


                MeasureUnconstrainedViewWidth(
                    viewToMeasure = {
                        Text(
                            danmakuItem.content,
                            style = TextStyle(
                                fontSize = danmakuItem.fontSize.sp,
                                color = Color.White
//                                    Color(danmakuItem.color)
                            ), maxLines = 1
                        )
                    }
                ) {
                    val fullWidth = it + maxWidth

                    val xOffset = maxWidth - fullWidth * (1f - percent / 100f)

                    LaunchedEffect(danmakuItem.id) {
                        move = true
                    }


                    Text(
                        danmakuItem.content,
                        modifier = Modifier.offset(
                            x = xOffset, y = maxHeight * (danmakuItem.startY / 100f)
                        ),
                        style = TextStyle(
                            fontSize = danmakuItem.fontSize.sp,
                            color = Color.White
//                                Color(danmakuItem.color)
                        ), maxLines = 1
                    )
                }
            }

        }
    }
}

@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints()).width.toDp()

        val contentPlaceable = subcompose("content") {
            content(measuredWidth)
        }[0].measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}


@Serializable
data class DanmakuItem(
    val id: String,
    val color: Int,
    val content: String,
    val fontSize: Int,
    val startY: Int = Random.nextInt(0, 90),
    /**
     * 动画总时间 毫秒
     */
    val duration: Long = 10000,
    var percent: Int = 100
)
