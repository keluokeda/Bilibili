package com.ke.biliblli.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository,
    private val bilibiliStorage: BilibiliStorage
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
                _uiState.update {
                    when (it) {

                        is VideoInfoState.Success -> {
                            return@update it.copy(expanded = action.expanded)
                        }

                        else -> {
                            return@update it
                        }
                    }

                }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = VideoInfoState.Loading
            try {
                val data = bilibiliRepository.videoInfo(params.bvid).data
                _uiState.value = VideoInfoState.Success(data!!, false)
                if (bilibiliStorage.directPlay) {
                    _event.send(
                        VideoInfoEvent.ToVideoDetail(
                            Screen.VideoDetail(
                                bvid = data.view.bvid,
                                cid = data.view.cid,
                                aid = data.view.aid
                            )
                        )
                    )
                }
            } catch (e: Exception) {
//                e.printStackTrace()
                CrashHandler.handler(e)
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

    data class ToVideoDetail(val target: Screen.VideoDetail) : VideoInfoEvent
}