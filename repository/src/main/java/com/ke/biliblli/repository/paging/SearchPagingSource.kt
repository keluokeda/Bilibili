package com.ke.biliblli.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.SearchResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler

@OptIn(ExperimentalPagingApi::class)
class SearchPagingSource(
    private val bilibiliRepository: BilibiliRepository,
    private val keywords: String,
) : PagingSource<Int, SearchResponse>() {
    override fun getRefreshKey(state: PagingState<Int, SearchResponse>): Int {
        return 1
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResponse> {

        if (keywords.isEmpty()) {

            return LoadResult.Page(emptyList(), null, null)

        }

        return try {
            val index = params.key ?: 1
            val list = bilibiliRepository.search(keywords, index).data!!.result
            LoadResult.Page(
                data = list,
                prevKey = null,
                nextKey = if (list.isNotEmpty()) (index + 1) else 2
            )
        } catch (e: Exception) {
            CrashHandler.handler(e)
            LoadResult.Error(e)
        }
    }


}