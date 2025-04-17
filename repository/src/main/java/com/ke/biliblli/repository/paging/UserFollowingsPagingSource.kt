package com.ke.biliblli.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.UserResponse
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler

class UserFollowingsPagingSource(
    private val bilibiliRepository: BilibiliRepository,
    private val id: Long
) : PagingSource<Int, UserResponse>() {
    override fun getRefreshKey(state: PagingState<Int, UserResponse>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserResponse> {


        if (id == 0L) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        return try {


            val index = params.key ?: 1

            val data = bilibiliRepository.userFollowings(id, index = index, size = 20).data!!

            LoadResult.Page(
                data = data.list,
                prevKey = null,
                nextKey = if (data.list.isNotEmpty()) (index + 1) else null
            )
        } catch (e: Exception) {
            CrashHandler.handler(e)
            LoadResult.Error(e)
        }
    }
}