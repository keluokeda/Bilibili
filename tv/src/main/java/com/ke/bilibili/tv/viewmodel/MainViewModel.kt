package com.ke.bilibili.tv.viewmodel

import com.ke.bilibili.tv.ui.MainTab
import com.ke.biliblli.common.event.MainTabChanged
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<Unit, MainAction, MainEvent>(Unit) {
    override fun handleAction(action: MainAction) {
        when (action) {
            is MainAction.Refresh -> {
//                viewModelScope.launch {
//                    _event.send(MainEvent.Refresh(action.current))
//                }
                EventBus.getDefault().post(MainTabChanged(action.current.index))
            }
        }
    }
}

sealed interface MainAction {
    data class Refresh(val current: MainTab) : MainAction

}

sealed interface MainEvent {
}