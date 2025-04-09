package com.ke.biliblli.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.HomeRecommendResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.repository.paging.HomeRecommendVideoPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeRecommendViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : ViewModel() {

    val videoListFlow = Pager<Int, HomeRecommendResponse>(
        config = PagingConfig(pageSize = 20)
    ) {
        HomeRecommendVideoPagingSource(bilibiliRepository)
    }.flow.cachedIn(viewModelScope)


}