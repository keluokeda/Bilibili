package com.ke.bilibili.tv.ui.component

import androidx.annotation.OptIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.ke.bilibili.tv.ui.theme.BilibiliTheme
import com.ke.biliblli.viewmodel.VideoDetailViewModel

//
//
//@Composable
//fun DanmakuView(
//    modifier: Modifier,
//    sender: ((DanmakuItem) -> Unit) -> Unit
//) {
//
//    var danmakuItemList by remember {
//        mutableStateOf<List<DanmakuItem>>(emptyList())
//    }
//
//    LaunchedEffect(sender) {
//        sender {
//            danmakuItemList += it
//        }
//
//    }
//
//
//
//
//    BoxWithConstraints(modifier = modifier) {
//        this.constraints.maxHeight
//
//        danmakuItemList.forEach { item ->
//
//
//            var danmakuItem by remember(item.id) {
//                mutableStateOf<DanmakuItem>(item)
//            }
//
//            var move by remember(danmakuItem.id) { mutableStateOf(false) }
//
//
//            val percent by animateFloatAsState(
//                targetValue = if (move) 0f else 1f, animationSpec = tween(
//                    durationMillis = danmakuItem.duration.toInt(),
//                    easing = LinearEasing
//                ), label = danmakuItem.id
//            )
//
//
//
//            if (percent == 0f) {
//                item.percent = 0
//            }
//
//            var fullWidth by remember {
//                mutableStateOf(0.dp)
//            }
//
//
//            if (item.percent != 0) {
//
//                if (fullWidth == 0.dp) {
//                    MeasureUnconstrainedViewWidth(
//                        viewToMeasure = {
//                            Text(
//                                danmakuItem.content,
//                                style = TextStyle(
//                                    fontSize = danmakuItem.fontSize.sp,
//                                    color = Color.White
////                                    Color(danmakuItem.color)
//                                ), maxLines = 1
//                            )
//                        }
//                    ) {
//                        val fullWidth = it + maxWidth
//
//                        val xOffset = maxWidth - fullWidth * (1f - percent)
//
//                        LaunchedEffect(danmakuItem.id) {
//                            move = true
//                        }
//
//
//                        Text(
//                            danmakuItem.content,
//                            modifier = Modifier.offset(
//                                x = xOffset, y = maxHeight * (danmakuItem.startY / 100f)
//                            ),
//                            style = TextStyle(
//                                fontSize = danmakuItem.fontSize.sp,
//                                color = Color(
//                                    red = item.color.first,
//                                    green = item.color.second,
//                                    blue = item.color.third
//                                )
////                                Color(danmakuItem.color)
//                            ), maxLines = 1
//                        )
//                    }
//                } else {
//                    val xOffset = maxWidth - fullWidth * (1f - percent)
//
//                    Text(
//                        danmakuItem.content,
//                        modifier = Modifier.offset(
//                            x = xOffset, y = maxHeight * (danmakuItem.startY / 100f)
//                        ),
//                        style = TextStyle(
//                            fontSize = danmakuItem.fontSize.sp,
//                            color = Color.White
////                                Color(danmakuItem.color)
//                        ), maxLines = 1
//                    )
//                }
//
//
//            }
//
//        }
//    }
//}

//@Composable
//fun MeasureUnconstrainedViewWidth(
//    viewToMeasure: @Composable () -> Unit,
//    content: @Composable (measuredWidth: Dp) -> Unit,
//) {
//    SubcomposeLayout { constraints ->
//        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0]
//            .measure(Constraints()).width.toDp()
//
//        val contentPlaceable = subcompose("content") {
//            content(measuredWidth)
//        }[0].measure(constraints)
//        layout(contentPlaceable.width, contentPlaceable.height) {
//            contentPlaceable.place(0, 0)
//        }
//    }
//}

//
//@Serializable
//data class DanmakuItem(
//    val id: String,
//    val color: Triple<Int, Int, Int>,
//    val content: String,
//    val fontSize: Int,
//    val startY: Int = Random.nextInt(0, 90),
//    /**
//     * 动画总时间 毫秒
//     */
//    val duration: Long = 10000,
//    var percent: Int = 100
//)

@OptIn(UnstableApi::class)
@Composable
fun DanmakuView(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<VideoDetailViewModel>()
    val danmakuItems by viewModel.danmakuItemsForDisplay.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//    viewModel.density = LocalDensity.current
//    }


    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                viewModel.onDanmakuViewGloballyPositioned(it)
            }) {
        this.constraints.maxHeight
        danmakuItems.forEach {

            key(it.id) {

                val content = @Composable {
                    Text(
                        it.content,
                        style = TextStyle(
                            color = it.fontColor.toColor(),
                            fontWeight = FontWeight.Bold,
                            fontSize = it.textSize
                        ),
                    )
                }

//                if (it.selfWidth == 0) {
//                    Box(
//                        modifier = Modifier
//                            .offset(
//                                x = maxWidth,
//                                y = maxHeight * (it.trackIndex / it.trackCount.toFloat())
//                            )
//                            .onGloballyPositioned { layout ->
////                                val position = layout.positionInParent().round()
//                                val size = layout.size
//                                viewModel.onGloballyPositioned(
//                                    it,
//                                    constraints.maxWidth,
//                                    constraints.maxHeight,
//                                    size
//                                )
//                            }
//                    ) {
//                        content()
//                    }
////                    }
//                } else {
                val offset by animateIntOffsetAsState(
                    IntOffset(
                        it.offsetX,
//                            (maxHeight.value * (it.trackIndex / it.trackCount.toFloat())).toInt()
                        (constraints.maxHeight * it.trackIndex / it.trackCount).toInt()
                    ).apply {
//                            Logger.d("x = $x , y = $y , it = $it")
                    },
                    label = "animateOffsetAsState",
                    animationSpec = tween(
                        durationMillis = it.duration.toInt(),
                        easing = LinearEasing
                    )
                )

                Box(
                    modifier = Modifier
                        .offset {
                            offset
                        }
                        .onGloballyPositioned { layout ->

                            viewModel.onGloballyPositioned(
                                it,
                                constraints.maxWidth,
                                constraints.maxHeight,
                                layout.size
                            )
//                            Logger.d(
//                                "onGloballyPositioned ${
//                                    layout.positionInParent().round()
//                                } ,${layout.size} $it"
//                            )
                        }) {
                    content()
                }
//                }
            }
        }
    }
}

@Composable
@Preview
private fun StrokeText() {
    BilibiliTheme {


        Text(
            "这是一段文字", style = MaterialTheme.typography.headlineSmall.copy(
                drawStyle = Stroke(width = 1f), color = Color.White
            ), modifier = Modifier
                .background(color = Color.Black)
                .padding(16.dp)
        )
    }
}


private fun Triple<Int, Int, Int>.toColor(): Color {
    return Color(first, second, third)
}