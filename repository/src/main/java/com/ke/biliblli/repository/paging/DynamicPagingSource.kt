package com.ke.biliblli.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.biliblli.api.response.DynamicItem
import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.CrashHandler

@OptIn(ExperimentalPagingApi::class)
class DynamicPagingSource(
    private val bilibiliRepository: BilibiliRepository,
    private val type: String,
    private val mid: Long?
) : PagingSource<String, DynamicItem>() {
    private var offset: String? = null
    override fun getRefreshKey(state: PagingState<String, DynamicItem>): String {
        offset = "init"
        return "init"
    }


    override suspend fun load(params: LoadParams<String>): LoadResult<String, DynamicItem> {
        return try {
            val response =
                bilibiliRepository.dynamicList(if (offset == "init") null else offset, type, mid)
            offset = response.data?.offset
            LoadResult.Page(data = response.data!!.list.filter {
                it.module.dynamic.major?.archive != null
            }, prevKey = null, nextKey = offset)
        } catch (e: Exception) {
            CrashHandler.handler(e)
            LoadResult.Error(e)
        }
    }


}