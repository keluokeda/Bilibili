package com.ke.biliblli.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.Screen
import com.ke.biliblli.db.dao.CommentDao
import com.ke.biliblli.db.entity.CommentEntity
import com.ke.biliblli.repository.paging.CommentsRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val initialSortType = VideoCommentSortType.Time

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val commentDao: CommentDao,
    bilibiliRepository: BilibiliRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val params = savedStateHandle.toRoute<Screen.Comment>()

    private val _sort = MutableStateFlow(initialSortType)

    val sort = _sort.asStateFlow()

    private val commentsRemoteMediator =
        CommentsRemoteMediator(
            bilibiliRepository,
            params.id,
            params.type,
            commentDao,
            sort.value.type
        )


    private val _event = Channel<VideoCommentsEvent>(capacity = Channel.CONFLATED)

    val event = _event.receiveAsFlow()

    @OptIn(ExperimentalPagingApi::class)
    val comments: Flow<PagingData<CommentEntity>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
        remoteMediator = commentsRemoteMediator
    ) {
        commentDao.getComments(params.id, params.type)
    }.flow.cachedIn(viewModelScope)

    fun toggleSort() {
        viewModelScope.launch {
            val newType =
                if (sort.value == VideoCommentSortType.Hot) VideoCommentSortType.Time else VideoCommentSortType.Hot

            commentsRemoteMediator.sort = newType.type
            _sort.value = newType

            _event.send(VideoCommentsEvent.Refresh)
        }
    }
}

sealed interface VideoCommentsEvent {
    data object Refresh : VideoCommentsEvent
}

enum class VideoCommentSortType(val type: Int, val leading: String, val training: String) {
    Time(0, "最新评论", "按时间"),
    Hot(1, "热门评论", "按热度")
}
