package com.ke.bilibili.tv.viewmodel

import androidx.lifecycle.viewModelScope
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<Unit, MainAction, MainEvent>(Unit) {
    override fun handleAction(action: MainAction) {
        when (action) {
            is MainAction.Refresh -> {
                viewModelScope.launch {
                    _event.send(MainEvent.Refresh(action.index))
                }
            }
        }
    }
}

sealed interface MainAction {
    data class Refresh(val index: Int) : MainAction

}

sealed interface MainEvent {
    data class Refresh(val index: Int) : MainEvent
}