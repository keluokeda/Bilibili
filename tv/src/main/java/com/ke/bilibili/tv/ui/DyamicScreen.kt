package com.ke.bilibili.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.FilterChip
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.bilibili.tv.observeWithLifecycle
import com.ke.bilibili.tv.ui.theme.BilibiliTheme
import com.ke.biliblli.api.response.DynamicItem
import com.ke.biliblli.api.response.DynamicItemModule
import com.ke.biliblli.api.response.DynamicItemModuleAuthor
import com.ke.biliblli.api.response.DynamicItemModuleDynamic
import com.ke.biliblli.api.response.DynamicItemModuleDynamicMajorArchive
import com.ke.biliblli.common.Screen
import com.ke.biliblli.viewmodel.DynamicType
import com.ke.biliblli.viewmodel.DynamicViewModel
import kotlinx.coroutines.launch


@Composable
internal fun DynamicRoute(
    state: LazyStaggeredGridState,
    navigate: (Any) -> Unit
) {
    val viewModel = hiltViewModel<DynamicViewModel>()
    val scope = rememberCoroutineScope()
    val list = viewModel.dynamicListFlow.collectAsLazyPagingItems()
    val currentType by viewModel.currentType.collectAsStateWithLifecycle()
    viewModel.event.observeWithLifecycle {
        list.refresh()
        state.scrollToItem(0)

    }
    DynamicScreen(list, state, currentType, navigate) {
        viewModel.updateType(it)
        list.refresh()
        scope.launch {
            state.scrollToItem(0)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun DynamicScreen(
    list: LazyPagingItems<DynamicItem>,
    state: LazyStaggeredGridState,
    currentType: DynamicType,
    navigate: (Any) -> Unit,
    setType: (DynamicType) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    16.dp
                ), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DynamicType.entries.forEach {
                FilterChip(selected = it == currentType, onClick = {
                    setType(it)
                }) {
                    Text(it.displayName)
                }
            }

        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            contentPadding = PaddingValues(24.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = state
        ) {
            items(list.itemCount) {
                val item = list[it]!!


                DynamicCard(item, navigate)


            }
        }
    }


}

@Composable
private fun DynamicCard(item: DynamicItem, navigate: (Any) -> Unit = {}) {

    var params by remember(item) {
        mutableStateOf<Any>(Unit)
    }

    Card(onClick = {
        if (params != Unit) {
            navigate(params)
        }
    }, modifier = Modifier) {

        DynamicContent(item) {
            params = it
        }

    }
}

@Composable
private fun DynamicContent(
    item: DynamicItem,
    paramsCallback: ((Any) -> Unit)? = null
) {
    val supportingContent =
        item.module.author.time + (if (item.module.author.time.isNotEmpty() && item.module.author.action.isNotEmpty()) "-" else "") + item.module.author.action

    ListItem(
        selected = true, onClick = {},
        headlineContent = {
            Text(item.module.author.name)
        },
        supportingContent = if (supportingContent.isEmpty()) null else {
            @Composable {
                Text(supportingContent)
            }
        },
        leadingContent = {
            AsyncImage(
                model = item.module.author.face,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        },
//        trailingContent = {
//            if (item.module.author.following == true) {
//                Button(onClick = {
//
//                }) {
//                    Text("已关注")
//                }
//            } else if (item.module.author.following == false) {
//                OutlinedButton(onClick = {
//
//                }) {
//                    Text("关注")
//                }
//            }
//        }
    )

    if (item.module.dynamic.major?.archive != null) {
        val archive = item.module.dynamic.major!!.archive!!
        DynamicVideoCard(archive)
        LaunchedEffect(Unit) {
            paramsCallback?.invoke(Screen.VideoDetail(archive.bvid))
        }
    } else if (item.module.dynamic.major?.live != null) {
        val json = item.module.dynamic.major?.live?.json()!!
        AsyncImage(
            model = json.cover, contentDescription = null, modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text(json.title, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    } else if (item.module.dynamic.major?.draw != null) {
        Text(
            item.module.dynamic.desc?.text ?: "",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = textMaxLine
        )
    } else if (item.module.dynamic.major?.article != null) {
        val article = item.module.dynamic.major!!.article!!
        ListItem(selected = false, onClick = {}, headlineContent = {
            Text(article.title)
        }, supportingContent = {
            Text(article.desc, maxLines = textMaxLine)
        })
    } else if (item.orig != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .background(Color.Black.copy(alpha = 0.7f))
        ) {
            DynamicContent(item.orig!!)
        }
    } else if (item.type == "DYNAMIC_TYPE_WORD" && item.module.dynamic.desc != null) {
        Text(
            item.module.dynamic.desc?.text ?: "",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = textMaxLine
        )
    } else if (item.module.dynamic.major?.pgc != null) {
        val pgc = item.module.dynamic.major!!.pgc!!

        AsyncImage(
            model = pgc.cover, contentDescription = null, modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text(pgc.title, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    } else {
        Text(item.module.dynamic.major?.toString() ?: item.type)
    }
}

private val textMaxLine = 4

@Composable
private fun DynamicVideoCard(archive: DynamicItemModuleDynamicMajorArchive) {

    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = archive.cover,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Text(archive.title, modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp))
    }
}

@Composable
@Preview(device = Devices.TV_1080p)
private fun DynamicCardPreview() {

    val item = DynamicItem(
        "", "", true, DynamicItemModule(
            author = DynamicItemModuleAuthor("", false, "汉库克", 100, "投稿了视频", "1分钟前"),
            dynamic = DynamicItemModuleDynamic(null)
        )
    )

    BilibiliTheme {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                DynamicCard(item)
            }
        }
    }
}
