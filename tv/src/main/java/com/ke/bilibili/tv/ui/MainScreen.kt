package com.ke.bilibili.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.ke.bilibili.tv.viewmodel.MainAction
import com.ke.bilibili.tv.viewmodel.MainViewModel
import com.ke.biliblli.common.Screen

private val tabs = listOf("推荐", "热门", "排行")

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainRoute(toVideoDetail: (Screen.VideoDetail) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }

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



