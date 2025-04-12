package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import com.ke.biliblli.api.response.LaterWatchVideo
import com.ke.biliblli.common.BilibiliRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LaterWatchViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) :
    BaseViewModel<LaterWatchState, Unit, Unit>(LaterWatchState.Loading) {
    override fun handleAction(action: Unit) {
        refresh()
    }

    init {
        refresh()
    }


    private fun refresh() {
        _uiState.value = LaterWatchState.Loading
        viewModelScope.launch {
            try {
                _uiState.value =
                    LaterWatchState.Success(list = bilibiliRepository.laterWatchList().data!!.list)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = LaterWatchState.Error
            }
        }
    }
}

sealed interface LaterWatchState {
    data object Loading : LaterWatchState

    data class Success(val list: List<LaterWatchVideo>) : LaterWatchState

    data object Error : LaterWatchState
}
