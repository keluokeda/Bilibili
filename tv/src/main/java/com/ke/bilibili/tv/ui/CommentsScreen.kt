package com.ke.bilibili.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.biliblli.common.Screen
import com.ke.biliblli.db.entity.CommentEntity
import com.ke.biliblli.viewmodel.CommentsViewModel

@Composable
fun CommentsRoute(navigate: (Any) -> Unit) {
    val viewModel = hiltViewModel<CommentsViewModel>()
    val comments = viewModel.comments.collectAsLazyPagingItems()


    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(comments.itemCount) {
            val comment = comments[it]!!


            CommentView(comment) {
                navigate(
                    Screen.UserDetail(
                        id = comment.userId,
                        name = comment.username,
                        avatar = comment.avatar,
                        sign = comment.userSign
                    )
                )
            }
        }
    }
}

@Composable
private fun CommentView(comment: CommentEntity, onClick: () -> Unit = {}) {
    Card(onClick) {
        ListItem(
            selected = false, onClick = {},
            headlineContent = {
                Text(comment.username)
            },
            leadingContent = {
                AsyncImage(
                    model = comment.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(
                            CircleShape
                        )
                        .background(Color.Gray), contentScale = ContentScale.Crop
                )
            },
            supportingContent = {
                Text(comment.timeDesc + " " + comment.ip)
            }
        )

        Text(
            comment.content, modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )

    }
}
//
//@Preview
//@Composable
//private fun CommentViewPreview() {
//    BilibiliTheme {
//        val comment = CommentEntity(
//            commentId = 0, oid = 0, type = 0, username = "汉库克",
//            id = "",
//            avatar = "",
//            parent = 0,
//            liked = false,
//            like = 100,
//            date = 0,
//            ip = "北京",
//            timeDesc = "10分钟前",
//            content = "这个世界太乱",
//        )
//        CommentView(comment)
//    }
//}