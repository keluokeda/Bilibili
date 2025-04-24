package com.ke.biliblli.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.biliblli.api.response.PageResponse
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TvVideoInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository,
    private val bilibiliStorage: BilibiliStorage
) : BaseViewModel<TvVideoInfoState, Unit, ToVideoDetail>(TvVideoInfoState.Loading) {

    private val bvid = savedStateHandle.toRoute<Screen.VideoInfo>().bvid
    override fun handleAction(action: Unit) {
        refresh()
    }

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = TvVideoInfoState.Loading
            try {
                val pageList = bilibiliRepository.pageList(bvid)
                _uiState.value = TvVideoInfoState.Success(
                    bilibiliRepository.videoInfo(bvid).data!!.apply {
                        if (bilibiliStorage.directPlay) {
                            _event.send(
                                ToVideoDetail(
                                    Screen.VideoDetail(
                                        bvid = view.bvid,
                                        cid = view.cid,
                                        aid = view.aid
                                    )
                                )
                            )
                        }
                    },
                    pageList.data ?: emptyList()
                )
            } catch (e: Exception) {
                CrashHandler.handler(e)
                _uiState.value = TvVideoInfoState.Error
            }
        }
    }


}

sealed interface TvVideoInfoState {
    data object Loading : TvVideoInfoState

    data object Error : TvVideoInfoState


    data class Success(val info: VideoInfoResponse, val pageList: List<PageResponse>) :
        TvVideoInfoState
}

data class ToVideoDetail(val detail: Screen.VideoDetail)
