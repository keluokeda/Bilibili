package com.ke.biliblli.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.SeasonsArchiveResponse
import com.ke.biliblli.api.response.UserResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.Screen
import com.ke.biliblli.repository.paging.UserFansPagingSource
import com.ke.biliblli.repository.paging.UserFollowingsPagingSource
import com.ke.biliblli.repository.paging.UserVideosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<UserDetailState, UserDetailAction, Unit>(
    UserDetailState(0L, "", "", "", UserDetailItem.Video)
) {

    private val userDetailParams = savedStateHandle.toRoute<Screen.UserDetail>()

    init {
        _uiState.update {
            it.copy(
                id = userDetailParams.id,
                name = userDetailParams.name,
                avatar = userDetailParams.avatar,
                sign = userDetailParams.sign,
                selected = UserDetailItem.Video
            )
        }
    }

    val userVideos = Pager<Int, SeasonsArchiveResponse>(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        UserVideosPagingSource(bilibiliRepository, userDetailParams.id)
    }.flow.cachedIn(viewModelScope)

    val userFans = Pager<Int, UserResponse>(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        UserFansPagingSource(bilibiliRepository, userDetailParams.id)
    }.flow.cachedIn(viewModelScope)


    val userFollowings = Pager<Int, UserResponse>(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        UserFollowingsPagingSource(bilibiliRepository, userDetailParams.id)
    }.flow.cachedIn(viewModelScope)

    override fun handleAction(action: UserDetailAction) {
        when (action) {
            is UserDetailAction.SelectItem -> {
                _uiState.update {
                    it.copy(selected = action.item)
                }
            }
        }
    }
}

data class UserDetailState(
    val id: Long,
    val avatar: String,
    val name: String,
    val sign: String,
    val selected: UserDetailItem
)

sealed interface UserDetailAction {
    data class SelectItem(val item: UserDetailItem) : UserDetailAction
}

enum class UserDetailItem(val displayName: String) {
    Video("投稿"),
    Seasons("合集"),
    Followers("粉丝"),
    Following("关注")
}

