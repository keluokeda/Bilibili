package com.ke.biliblli.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.DynamicItem
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.repository.paging.DynamicPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DynamicAllViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository
) : ViewModel() {


    val dynamicListFlow = Pager<String, DynamicItem>(
        config = PagingConfig(pageSize = 20)
    ) {
        DynamicPagingSource(bilibiliRepository, "all")
    }.flow.cachedIn(viewModelScope)
}