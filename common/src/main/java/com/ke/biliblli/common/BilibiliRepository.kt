package com.ke.biliblli.common

import com.ke.biliblli.api.response.BaseResponse
import com.ke.biliblli.api.response.CommentResponse
import com.ke.biliblli.api.response.DynamicResponse
import com.ke.biliblli.api.response.HistoryResponse
import com.ke.biliblli.api.response.HomeRecommendListResponse
import com.ke.biliblli.api.response.LaterWatchResponse
import com.ke.biliblli.api.response.LoginInfoResponse
import com.ke.biliblli.api.response.PollQrcodeResponse
import com.ke.biliblli.api.response.QrCodeResponse
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.api.response.VideoUrlResponse
import com.ke.biliblli.api.response.VideoViewResponse

interface BilibiliRepository {

    suspend fun homeRecommendVideos(index: Int): BaseResponse<HomeRecommendListResponse>


    suspend fun videoUrl(
        cid: Long, bvid: String
    ): BaseResponse<VideoUrlResponse>


    suspend fun videoView(bvid: String): BaseResponse<VideoViewResponse>


    suspend fun history(
        max: Long?,
        business: String?,
        at: Long?,
        type: String? = "archive"
    ): BaseResponse<HistoryResponse>

    suspend fun loginQrCode(): BaseResponse<QrCodeResponse>

    suspend fun checkLogin(key: String): BaseResponse<PollQrcodeResponse>

    suspend fun loginIngo(): BaseResponse<LoginInfoResponse>


    suspend fun videoInfo(
        bvid: String
    ): BaseResponse<VideoInfoResponse>


    suspend fun laterWatchList(): BaseResponse<LaterWatchResponse>

    suspend fun comments(index: Int, oid: Long, type: Int, sort: Int): BaseResponse<CommentResponse>


    suspend fun dynamicList(
        offset: String? = null,
        type: String = "all"
    ): BaseResponse<DynamicResponse>
}