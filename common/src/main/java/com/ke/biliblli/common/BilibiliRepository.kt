package com.ke.biliblli.common

import com.ke.biliblli.api.response.BaseResponse
import com.ke.biliblli.api.response.CommentResponse
import com.ke.biliblli.api.response.HomeRecommendListResponse
import com.ke.biliblli.api.response.LoginInfoResponse
import com.ke.biliblli.api.response.PollQrcodeResponse
import com.ke.biliblli.api.response.QrCodeResponse
import com.ke.biliblli.api.response.VideoDetailResponse
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.api.response.VideoUrlResponse
import retrofit2.http.Query

interface BilibiliRepository {

    suspend fun homeRecommendVideos(index: Int): BaseResponse<HomeRecommendListResponse>


    suspend fun videoUrl(
        cid: Long, bvid: String
    ): BaseResponse<VideoUrlResponse>


    suspend fun loginQrCode(): BaseResponse<QrCodeResponse>

    suspend fun checkLogin(key: String): BaseResponse<PollQrcodeResponse>

    suspend fun loginIngo(): BaseResponse<LoginInfoResponse>


    suspend fun videoInfo(
        bvid: String
    ): BaseResponse<VideoInfoResponse>


    suspend fun comments(index: Int, oid: Long, type: Int,sort: Int): BaseResponse<CommentResponse>
}