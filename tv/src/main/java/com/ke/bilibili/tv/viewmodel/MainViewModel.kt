package com.ke.bilibili.tv.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.bilibili.tv.ui.MainTab
import com.ke.biliblli.api.BilibiliApi
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
    private val bilibiliApi: BilibiliApi
) : BaseViewModel<Unit, MainAction, MainEvent>(Unit) {
    private val userId = savedStateHandle.toRoute<Screen.Main>().userId

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
//                bilibiliRepository.userFollowers(userId)
//                bilibiliRepository.userFollowings(userId)
//                bilibiliRepository.userRelationStatus(userId)
//                bilibiliRepository.userNavNum(userId)
//                bilibiliApi.userNavNum(19519514)
//                bilibiliApi.request("https://api.bilibili.com/x/polymer/web-space/home/seasons_series?mid=19519514&page_num=1&page_size=20")
//                bilibiliApi.seasonsSeriesList(19519514)
                bilibiliApi.request("https://api.bilibili.com/x/series/recArchivesByKeywords?mid=19519514&keywords=")
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