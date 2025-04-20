package com.ke.biliblli.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.HomeRecommendResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.event.MainTab
import com.ke.biliblli.common.event.MainTabChanged
import com.ke.biliblli.repository.paging.HomeRecommendVideoPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@HiltViewModel
class HomeRecommendViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : BaseViewModel<Unit, Unit, Unit>(Unit) {

    val videoListFlow = Pager<Int, HomeRecommendResponse>(
        config = PagingConfig(pageSize = 20)
    ) {
        HomeRecommendVideoPagingSource(bilibiliRepository)
    }.flow.cachedIn(viewModelScope)

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onTabChanged(event: MainTabChanged) {
        if (event.tab == MainTab.Recommend) {
            viewModelScope.launch {
                _event.send(Unit)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    override fun handleAction(action: Unit) {

    }
}