package com.ke.biliblli.common.http

import com.ke.bilibili.common.entity.DmSegMobileReply
import retrofit2.http.GET
import retrofit2.http.Query

interface BilibiliProtoApi {

    @GET("x/v2/dm/web/seg.so")
    suspend fun dm(
        @Query("type") type: Int = 1,
        @Query("oid") oid: Long,
        @Query("segment_index") index: Int
    ): DmSegMobileReply
}