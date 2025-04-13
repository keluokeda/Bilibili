package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import com.ke.biliblli.api.response.LaterWatchVideo
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.common.event.MainTabChanged
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
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
        EventBus.getDefault().register(this)

    }


    @Subscribe
    fun onTabChanged(event: MainTabChanged) {
        if (event.index == 3) {
            refresh()
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }


    private fun refresh() {
        _uiState.value = LaterWatchState.Loading
        viewModelScope.launch {
            try {
                _uiState.value =
                    LaterWatchState.Success(list = bilibiliRepository.laterWatchList().data!!.list)
            } catch (e: Exception) {
//                e.printStackTrace()
                CrashHandler.handler(e)
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
