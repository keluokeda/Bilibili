package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.DynamicItem
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.event.MainTabChanged
import com.ke.biliblli.repository.paging.DynamicPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : BaseViewModel<Unit, Unit, Unit>(Unit) {
    private val _currentType = MutableStateFlow(DynamicType.All)
    val currentType = _currentType.asStateFlow()


    override fun handleAction(action: Unit) {

    }

    init {
        EventBus.getDefault().register(this)
    }


    @Subscribe
    fun onTabChanged(event: MainTabChanged) {
        if (event.index == 1) {
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
        DynamicPagingSource(bilibiliRepository, currentType.value.type)
    }.flow.cachedIn(viewModelScope)
}