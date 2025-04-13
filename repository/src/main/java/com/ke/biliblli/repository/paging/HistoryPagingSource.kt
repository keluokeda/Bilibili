package com.ke.biliblli.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.HistoryCursor
import com.ke.biliblli.api.response.HistoryItem
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler
import java.util.UUID

class HistoryPagingSource(
    private val bilibiliRepository: BilibiliRepository,
    private val type: String = "archive"
) : PagingSource<String, HistoryItem>() {

    private var cursor: HistoryCursor? = null
    override fun getRefreshKey(state: PagingState<String, HistoryItem>): String? {
        cursor = null
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, HistoryItem> {

        return try {
            val response =
                bilibiliRepository.history(cursor?.max, cursor?.business, cursor?.at, type)
            LoadResult.Page(
                data = response.data!!.list,
                prevKey = null,
                nextKey = if (response.data?.cursor == null) null else UUID.randomUUID().toString()
            )
        } catch (e: Exception) {
            CrashHandler.handler(e)
            LoadResult.Error(e)
        }
    }
}