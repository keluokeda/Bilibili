package com.ke.biliblli.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.SeasonsResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler

class UserSeasonsPagingSource(
    private val bilibiliRepository: BilibiliRepository,
    private val id: Long
) : PagingSource<Int, SeasonsResponse>() {
    override fun getRefreshKey(state: PagingState<Int, SeasonsResponse>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SeasonsResponse> {


        if (id == 0L) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        return try {


            val index = params.key ?: 1

            val data = bilibiliRepository.seasonsSeriesList(id, index = index, size = 20).data!!

            LoadResult.Page(
                data = data.itemsLists.seasonsList,
                prevKey = null,
                nextKey = if (data.itemsLists.seasonsList.isNotEmpty()) (index + 1) else null
            )
        } catch (e: Exception) {
            CrashHandler.handler(e)
            LoadResult.Error(e)
        }
    }
}