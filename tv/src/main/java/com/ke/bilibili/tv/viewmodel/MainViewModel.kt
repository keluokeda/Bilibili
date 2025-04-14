package com.ke.bilibili.tv.viewmodel

import androidx.lifecycle.viewModelScope
import com.ke.bilibili.tv.ui.MainTab
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.event.MainTabChanged
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<Unit, MainAction, MainEvent>(Unit) {
    override fun handleAction(action: MainAction) {
        when (action) {
            is MainAction.Refresh -> {
                EventBus.getDefault().post(MainTabChanged(action.current.index))
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
//                val dm = bilibiliRepository.dm(1, 1176840, 1)
//                dm.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

sealed interface MainAction {
    data class Refresh(val current: MainTab) : MainAction

}

sealed interface MainEvent