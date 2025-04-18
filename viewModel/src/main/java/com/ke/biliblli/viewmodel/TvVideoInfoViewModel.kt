package com.ke.biliblli.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TvVideoInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<TvVideoInfoState, Unit, Unit>(TvVideoInfoState.Loading) {

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
                _uiState.value = TvVideoInfoState.Success(bilibiliRepository.videoInfo(bvid).data!!)
            } catch (e: Exception) {
                CrashHandler.handler(e)
                _uiState.value = TvVideoInfoState.Error
            }
        }
    }

    init {

    }


}

sealed interface TvVideoInfoState {
    data object Loading : TvVideoInfoState

    data object Error : TvVideoInfoState


    data class Success(val info: VideoInfoResponse) : TvVideoInfoState
}