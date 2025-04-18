package com.ke.bilibili.tv.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.bilibili.tv.ui.MainTab
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.BilibiliStorage
import com.ke.biliblli.common.Screen
import com.ke.biliblli.common.event.MainTabChanged
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
//    private val bilibiliApi: BilibiliApi,
    private val bilibiliRepository: BilibiliRepository,
    private val bilibiliStorage: BilibiliStorage
) : BaseViewModel<Unit, MainAction, MainEvent>(Unit) {
    private val userId = savedStateHandle.toRoute<Screen.Main>().userId

    val defaultTab = MainTab.entries.first { it.index == bilibiliStorage.mainDefaultTab }
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
                bilibiliRepository.search("艾尔登法环", 1)
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