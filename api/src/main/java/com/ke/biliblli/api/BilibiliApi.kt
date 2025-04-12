package com.ke.biliblli.api

import com.ke.biliblli.api.response.BaseResponse
import com.ke.biliblli.api.response.CommentResponse
import com.ke.biliblli.api.response.HomeRecommendListResponse
import com.ke.biliblli.api.response.LoginInfoResponse
import com.ke.biliblli.api.response.PollQrcodeResponse
import com.ke.biliblli.api.response.QrCodeResponse
import com.ke.biliblli.api.response.VideoDetailResponse
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.api.response.VideoUrlResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BilibiliApi {


    /**
     * 首页推荐视频
     */
    @GET("x/web-interface/index/top/feed/rcmd")
    suspend fun homeRecommendVideoList(
        @Query("fresh_idx") index: Int = 0,
        @Query("brush") brush: Int = 0,
        @Query("version") version: Int = 1,
        @Query("feed_version") feedVersion: String = "V3",
        @Query("homepage_ver") homepageVersion: Int = 1,
        @Query("ps") pageSize: Int = 20,
        @Query("fresh_type") freshType: Int = 4
    ): BaseResponse<HomeRecommendListResponse>

    /**
     * 获取视频地址
     */
    @GET("x/player/wbi/playurl")
    suspend fun videoUrl(
        @Query("cid") cid: Long,
        @Query("qn") qn: Int ,
        @Query("fnval") fnval: Int ,
        @Query("bvid") bvid: String,
        @Query("fourk") fourk: Int = 1,
        @Query("voice_balance") voiceBalance: Int = 1,
        @Query("gaia_source") gaiaSource: String = "pre-load",
        @Query("web_location") webLocation: Int = 1550101,
        @Query("wts") wts: Long,
        @Query("w_rid") sign: String
    ): BaseResponse<VideoUrlResponse>

    /**
     * 获取签名参数
     */
    @GET("x/web-interface/nav")
    suspend fun loginInfo(): BaseResponse<LoginInfoResponse>

    /**
     * 获取扫码地址
     */
    @GET("https://passport.bilibili.com/x/passport-login/web/qrcode/generate")
    suspend fun generateQrcode(): BaseResponse<QrCodeResponse>

    /**
     * 检查扫码是否成功
     */
    @GET("https://passport.bilibili.com/x/passport-login/web/qrcode/poll")
    suspend fun pollQrcode(
        @Query("qrcode_key") key: String
    ): BaseResponse<PollQrcodeResponse>


    /**
     * 获取相关视频
     */
    @GET("x/web-interface/archive/related")
    suspend fun relatedVideos(
        @Query("bvid") bvid: String
    ): BaseResponse<List<VideoDetailResponse>>


    /**
     * 视频简介
     */
    @GET("https://api.bilibili.com/x/web-interface/wbi/view/detail")
    suspend fun videoInfo(
        @Query("bvid") bvid: String,
        @Query("wts") wts: Long,
        @Query("w_rid") sign: String
    ): BaseResponse<VideoInfoResponse>


    /**
     * 获取评论
     */
    @GET("x/v2/reply")
    suspend fun comments(
        @Query("oid") oid: Long,
        @Query("pn") index: Int,
        @Query("type") type: Int,
        @Query("sort") sort: Int
    ): BaseResponse<CommentResponse>


    @GET("x/web-interface/history/cursor")
    suspend fun history()

    companion object {
        const val baseUrl = "https://api.bilibili.com/"
    }
}