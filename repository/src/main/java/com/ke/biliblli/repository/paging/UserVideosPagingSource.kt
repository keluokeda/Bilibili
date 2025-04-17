package com.ke.biliblli.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.SeasonsArchiveResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler

class UserVideosPagingSource(
    private val bilibiliRepository: BilibiliRepository,
    private val id: Long
) : PagingSource<Int, SeasonsArchiveResponse>() {
    override fun getRefreshKey(state: PagingState<Int, SeasonsArchiveResponse>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SeasonsArchiveResponse> {


        if (id == 0L) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        return try {


            val index = params.key ?: 1

            val data = bilibiliRepository.userVideos(id, index = index, size = 20).data!!

            LoadResult.Page(
                data = data.archives,
                prevKey = null,
                nextKey = if (data.archives.isNotEmpty()) (index + 1) else null
            )
        } catch (e: Exception) {
            CrashHandler.handler(e)
            LoadResult.Error(e)
        }
    }
}