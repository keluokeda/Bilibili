package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.DynamicItem
import com.ke.biliblli.api.response.DynamicUpItem
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.common.event.MainTab
import com.ke.biliblli.common.event.MainTabChanged
import com.ke.biliblli.repository.paging.DynamicPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject


enum class DynamicType(val displayName: String, val type: String) {
    All("全部", "all"),
    Video("投稿", "video"),
    Pgc("番剧", "pgc"),
    Article("专栏", "article")
}

@HiltViewModel
class DynamicViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<DynamicState, DynamicAction, Unit>(DynamicState()) {
    private val _currentType = MutableStateFlow(DynamicType.Video)
    val currentType = _currentType.asStateFlow()

    private var mid: Long? = null

    override fun handleAction(action: DynamicAction) {
        when (action) {
            DynamicAction.Retry -> {

            }

            is DynamicAction.SelectedUp -> {
                _uiState.update {
                    it.copy(upList = it.upList.map { item ->
                        if (item != null && item == action.up) {
                            item.copy(hasUpdate = false)
                        } else {
                            item
                        }

                    }, current = action.up)
                }
                mid = action.up?.mid
            }
        }
    }

    init {
        EventBus.getDefault().register(this)
        refresh()
    }


    private fun refresh() {
        viewModelScope.launch {
            try {
                val list = mutableListOf<DynamicUpItem?>(null)

                list.addAll(bilibiliRepository.updateDynamicUpList().data!!.upList.items)

                _uiState.value = DynamicState(list, null)
            } catch (e: Exception) {
                CrashHandler.handler(e)
            }
        }
    }

    @Subscribe
    fun onTabChanged(event: MainTabChanged) {
        if (event.tab == MainTab.Dynamic) {
            viewModelScope.launch {
                _event.send(Unit)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    fun updateType(type: DynamicType) {
        if (currentType.value == type) {
            return
        }
        _currentType.value = type
    }


    val dynamicListFlow = Pager<String, DynamicItem>(
        config = PagingConfig(pageSize = 20)
    ) {
        DynamicPagingSource(bilibiliRepository, currentType.value.type, mid)
    }.flow.cachedIn(viewModelScope)
}

data class DynamicState(
    val upList: List<DynamicUpItem?> = emptyList(), val current: DynamicUpItem? = null
)

sealed interface DynamicAction {
    data object Retry : DynamicAction

    data class SelectedUp(val up: DynamicUpItem?) : DynamicAction
}