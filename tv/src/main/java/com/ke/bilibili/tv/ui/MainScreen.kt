package com.ke.bilibili.tv.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.ke.bilibili.tv.viewmodel.MainAction
import com.ke.bilibili.tv.viewmodel.MainViewModel

enum class MainTab(val displayName: String, val index: Int) {
    Recommend("推荐", 0),
    Dynamic("动态", 1),
    Fav("我的收藏", 2),
    LaterWatch("稍后再看", 3),
    History("历史记录", 4),
    Settings("设置", 5),
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainRoute(navigate: (Any) -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Recommend) }

    val mainViewModel = hiltViewModel<MainViewModel>()

    val focusRequester = remember { FocusRequester() }

    var hasFocus by remember {
        mutableStateOf(false)
    }


    BackHandler(enabled = !hasFocus) {
        focusRequester.requestFocus()
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
//            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = selectedTab.index,
            modifier = Modifier
                .padding(16.dp)
                .focusRestorer()
                .onFocusChanged {
                    hasFocus = it.hasFocus
                }
                .focusRequester(focusRequester)
//                .focusable(true, interactionSource)
        ) {
            MainTab.entries.forEachIndexed { index, tab ->
                key(index) {
                    Tab(selected = tab == selectedTab, onFocus = {
                        selectedTab = tab
                    }, onClick = {
                        mainViewModel.handleAction(MainAction.Refresh(tab))
                    }) {
                        Text(
                            text = tab.displayName,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        val dynamicState = rememberLazyStaggeredGridState()

        val recommendState = rememberLazyGridState()

        val historyState = rememberLazyGridState()


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            if (selectedTab == MainTab.Recommend) {
                RecommendVideosRoute(recommendState, navigate)
            } else if (selectedTab == MainTab.Dynamic) {
                DynamicRoute(dynamicState, navigate)
            } else if (selectedTab == MainTab.LaterWatch) {
                LaterWatchRoute(navigate)
            } else if (selectedTab == MainTab.History) {
                HistoryRoute(historyState, navigate)
            } else if (selectedTab == MainTab.Settings) {
                SettingsRoute()
            } else if (selectedTab == MainTab.Fav) {
                MyFavRoute(navigate)
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



