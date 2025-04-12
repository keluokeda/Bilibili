package com.ke.bilibili.tv.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.ke.bilibili.tv.viewmodel.MainAction
import com.ke.bilibili.tv.viewmodel.MainViewModel
import com.ke.biliblli.common.Screen

private val tabs = listOf("推荐", "热门", "排行", "稍后再看")

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainRoute(toVideoDetail: (Screen.VideoDetail) -> Unit) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val mainViewModel = hiltViewModel<MainViewModel>()

    Column(
        modifier = Modifier
            .fillMaxSize(),
//            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = selectedIndex,
            modifier = Modifier
                .padding(16.dp)
                .focusRestorer()
        ) {
            tabs.forEachIndexed { index, tab ->
                key(index) {
                    Tab(selected = index == selectedIndex, onFocus = {
                        selectedIndex = index
                    }, onClick = {
//                        Logger.d("tab onClick $index")
                        mainViewModel.handleAction(MainAction.Refresh(index))
                    }) {
                        Text(
                            text = tab,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            if (selectedIndex == 0) {
                RecommendVideosRoute(tabIndex = 0, toVideoDetail)
            } else if (selectedIndex == 1) {
                Column {

                    Button(onClick = {
                        toVideoDetail(
                            Screen.VideoDetail(
                                cid = 28104724389,
                                bvid = "BV1HEf2YWEvs",
                                id = 113898824998659
                            )
                        )
                    }) {
                        Text("【杜比视界】你的设备能撑住吗？影视飓风年度样片")
                    }

                    Button(onClick = {
                        toVideoDetail(
                            Screen.VideoDetail(
                                cid = 29348660006,
                                bvid = "BV1VDdmY6EXf",
                                id = 114317232047576
                            )
                        )
                    }) {
                        Text("点击体验杜比视界与杜比全景声呈现的风景盛宴，沉浸自然回响！")
                    }
                }

            } else if (selectedIndex == 3) {
                LaterWatchRoute(toVideoDetail)
            }
        }

//        Text(
//            selectedIndex.toString(), modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .background(
//                    Color.Red
//                )
//        )
    }


}



