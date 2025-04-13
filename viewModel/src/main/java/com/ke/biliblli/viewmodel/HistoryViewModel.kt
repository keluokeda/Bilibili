package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.HistoryItem
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.event.MainTabChanged
import com.ke.biliblli.repository.paging.HistoryPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<Unit, Unit, Unit>(Unit) {
    val listFlow = Pager<String, HistoryItem>(
        config = PagingConfig(pageSize = 20)
    ) {
        HistoryPagingSource(bilibiliRepository)
    }.flow.cachedIn(viewModelScope)

    init {
        EventBus.getDefault().register(this)
    }

    override fun handleAction(action: Unit) {

    }


    @Subscribe
    fun onTabChanged(event: MainTabChanged) {
        if (event.index == 3) {
            viewModelScope.launch {
                _event.send(Unit)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}