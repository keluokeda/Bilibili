package com.ke.bilibili.tv.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.biliblli.api.response.SearchHotWord
import com.ke.biliblli.api.response.SearchResponse
import com.ke.biliblli.api.response.SearchSuggestTag
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import com.ke.biliblli.common.Screen
import com.ke.biliblli.repository.paging.SearchPagingSource
import com.ke.biliblli.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            try {

                _uiState.update {
                    it.copy(hotWords = bilibiliRepository.searchKeyWords().list)
                }

            } catch (e: Exception) {
                CrashHandler.handler(e)
            }
        }

        viewModelScope.launch {
            bilibiliRepository.searchHistoryList().collect { list ->
                _uiState.update {
                    it.copy(historyList = list)
                }
            }
        }
    }

    val showKeyboardWhenStart = savedStateHandle.toRoute<Screen.Search>().defaultKeywords.isEmpty()

    val resultList = Pager<Int, SearchResponse>(
        config = PagingConfig(pageSize = 20)
    ) {
        SearchPagingSource(bilibiliRepository, uiState.value.keywords)
    }.flow.cachedIn(viewModelScope)

    override fun handleAction(action: SearchAction) {
        when (action) {
            is SearchAction.KeywordsChanged -> {
                _uiState.update {
                    it.copy(keywords = action.value)
                }

                updateSuggest()
            }

            SearchAction.Search -> {

            }
        }
    }

    private fun updateSuggest() {
        val keywords = uiState.value.keywords

        if (keywords.isEmpty()) {
            _uiState.update {
                it.copy(
                    suggestWords = emptyList()
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                val list = bilibiliRepository.searchSuggest(keywords).result.tag

                _uiState.update {
                    it.copy(
                        suggestWords = list
                    )
                }
            } catch (e: Exception) {
                CrashHandler.handler(e)
            }
        }
    }


}

data class SearchState(
    val keywords: String,
    val historyList: List<String> = emptyList(),
    val hotWords: List<SearchHotWord> = emptyList(),
    val suggestWords: List<SearchSuggestTag> = emptyList()
)

sealed interface SearchAction {
    data class KeywordsChanged(val value: String) : SearchAction

    data object Search : SearchAction
}
