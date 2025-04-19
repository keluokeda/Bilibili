package com.ke.bilibili.tv.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.SearchResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.Screen
import com.ke.biliblli.repository.paging.SearchPagingSource
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bilibiliRepository: BilibiliRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<SearchState, SearchAction, Unit>(
    SearchState(
        savedStateHandle.toRoute<Screen.Search>().defaultKeywords
    )
) {


    val resultList = Pager<Int, SearchResponse>(
        config = PagingConfig(pageSize = 20)
    ) {
        SearchPagingSource(bilibiliRepository, uiState.value.keywords)
    }.flow.cachedIn(viewModelScope)

    override fun handleAction(action: SearchAction) {
        when (action) {
            is SearchAction.KeywordsChanged -> {
                _uiState.value = SearchState(action.value)
            }

            SearchAction.Search -> {

            }
        }
    }


}

data class SearchState(
    val keywords: String
)

sealed interface SearchAction {
    data class KeywordsChanged(val value: String) : SearchAction

    data object Search : SearchAction
}
