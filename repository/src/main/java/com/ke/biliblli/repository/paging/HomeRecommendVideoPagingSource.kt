package com.ke.biliblli.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.HomeRecommendResponse
import com.ke.biliblli.common.BilibiliRepository

class HomeRecommendVideoPagingSource(
    private val bilibiliRepository: BilibiliRepository
) : PagingSource<Int, HomeRecommendResponse>() {
    override fun getRefreshKey(state: PagingState<Int, HomeRecommendResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeRecommendResponse> {
        return try {
            val index = params.key ?: 0
            val response = bilibiliRepository.homeRecommendVideos(index)
            LoadResult.Page(data = response.data!!.item, prevKey = null, nextKey = index + 1)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}