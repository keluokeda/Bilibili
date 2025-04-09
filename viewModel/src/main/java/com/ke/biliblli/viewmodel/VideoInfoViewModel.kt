package com.ke.biliblli.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<VideoInfoState, VideoInfoAction, VideoInfoEvent>(
    VideoInfoState.Loading
) {

    private val params = savedStateHandle.toRoute<Screen.VideoDetail>()

    init {
        refresh()
    }

    override fun handleAction(action: VideoInfoAction) {
        when (action) {
            VideoInfoAction.Retry -> {
                refresh()
            }

            is VideoInfoAction.SetExpanded -> {

            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = VideoInfoState.Loading
            try {
                val data = bilibiliRepository.videoInfo(params.bvid).data
                _uiState.value = VideoInfoState.Success(data!!, false)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = VideoInfoState.Error
            }
        }
    }
}

sealed interface VideoInfoState {
    data object Loading : VideoInfoState
    data object Error : VideoInfoState
    data class Success(val info: VideoInfoResponse, val expanded: Boolean = false) : VideoInfoState
}

sealed interface VideoInfoAction {
    data object Retry : VideoInfoAction
    data class SetExpanded(val expanded: Boolean) : VideoInfoAction
}

sealed interface VideoInfoEvent {
    data class ShowToast(val message: String) : VideoInfoEvent
}