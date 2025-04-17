package com.ke.biliblli.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.FavResourceMediaResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler

class FavResourceListPagingSource(
    private val bilibiliRepository: BilibiliRepository,
    private val id: Long
) : PagingSource<Int, FavResourceMediaResponse>() {
    override fun getRefreshKey(state: PagingState<Int, FavResourceMediaResponse>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavResourceMediaResponse> {


        if (id == 0L) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        return try {


            val index = params.key ?: 1

            val data = bilibiliRepository.favDetail(id, params.key ?: 1, 20).data!!

            LoadResult.Page(
                data = data.medias,
                prevKey = null,
                nextKey = if (data.more) (index + 1) else null
            )
        } catch (e: Exception) {
            CrashHandler.handler(e)
            LoadResult.Error(e)
        }
    }
}