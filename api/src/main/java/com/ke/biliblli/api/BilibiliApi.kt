package com.ke.biliblli.api

import com.ke.biliblli.api.request.Payload
import com.ke.biliblli.api.response.BaseResponse
import com.ke.biliblli.api.response.CommentResponse
import com.ke.biliblli.api.response.DynamicResponse
import com.ke.biliblli.api.response.DynamicUpListResponse
import com.ke.biliblli.api.response.EmptyJson
import com.ke.biliblli.api.response.FavResourceListResponse
import com.ke.biliblli.api.response.HistoryResponse
import com.ke.biliblli.api.response.HomeRecommendListResponse
import com.ke.biliblli.api.response.LaterWatchResponse
import com.ke.biliblli.api.response.LoginInfoResponse
import com.ke.biliblli.api.response.PageResponse
import com.ke.biliblli.api.response.PollQrcodeResponse
import com.ke.biliblli.api.response.QrCodeResponse
import com.ke.biliblli.api.response.RelationStatusResponse
import com.ke.biliblli.api.response.SearchListResponse
import com.ke.biliblli.api.response.SeasonsListDataResponse
import com.ke.biliblli.api.response.UserArchivesResponse
import com.ke.biliblli.api.response.UserFavListResponse
import com.ke.biliblli.api.response.UserInfoResponse
import com.ke.biliblli.api.response.UserListResponse
import com.ke.biliblli.api.response.VideoInfoResponse
import com.ke.biliblli.api.response.VideoUrlResponse
import com.ke.biliblli.api.response.VideoViewResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

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
        @Query("qn") qn: Int,
        @Query("fnval") fnval: Int,
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


//    /**
//     * 获取相关视频
//     */
//    @GET("x/web-interface/archive/related")
//    suspend fun relatedVideos(
//        @Query("bvid") bvid: String
//    ): BaseResponse<List<VideoDetailResponse>>


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


    /**
     * 播放历史
     */
    @GET("x/web-interface/history/cursor")
    suspend fun history(
        @Query("max") max: Long?,
        @Query("business") business: String?,
        @Query("view_at") at: Long?,
        @Query("type") type: String? = "archive"
    ): BaseResponse<HistoryResponse>

    /**
     * 稍后再看
     */
    @GET("x/v2/history/toview")
    suspend fun laterWatch(): BaseResponse<LaterWatchResponse>

    /**
     * 全部动态
     */
    @GET("x/polymer/web-dynamic/v1/feed/all")
    suspend fun dynamicList(
        @Query("offset") offset: String? = null,
        @Query("type") type: String = "all",
        @Query("host_mid") mid: Long? = null
    ): BaseResponse<DynamicResponse>


    @GET("x/web-interface/view")
    suspend fun videoView(@Query("bvid") bvid: String): BaseResponse<VideoViewResponse>

    /**
     * 更新了动态的up主
     */
    @GET("x/polymer/web-dynamic/v1/portal?up_list_more=1&web_location=333.1365")
    suspend fun updateDynamicUpList(): BaseResponse<DynamicUpListResponse>


    /**
     * 用户信息
     */
    @GET("x/space/wbi/acc/info")
    suspend fun userInfo(
        @Query("mid") mid: Long,
        @Query("platform") platform: String,
        @Query("web_location") location: String,
        @Query("token") token: String,
        @Query("wts") wts: Long,
        @Query("w_rid") sign: String
    ): BaseResponse<UserInfoResponse>

    @GET("x/relation/stat")
    suspend fun userRelationStatus(@Query("vmid") vmid: Long): BaseResponse<RelationStatusResponse>


    @POST("x/internal/gaia-gateway/ExClimbWuzhi")
    suspend fun activeBuvid(
        @Body payload: Payload
    ): BaseResponse<EmptyJson>


//    @GET("x/web-frontend/getbuvid")
//    suspend fun getBuvid(): BaseResponse<BuvidResponse>

    /**
     * 用户收藏夹
     */
    @GET("x/v3/fav/folder/created/list-all")
    suspend fun userFav(
        @Query("up_mid") mid: Long,
        @Query("type") type: Int = 2
    ): BaseResponse<UserFavListResponse>

//    @GET("x/v3/fav/folder/collected/list")
//    suspend fun userFavVideo(
//        @Query("up_mid") mid: Long,
//        @Query("ps") size: Int,
//        @Query("pn") index: Int,
//        @Query("platform") platform: String = "web"
//    )


    /**
     * 收藏夹详情
     */
    @GET("x/v3/fav/resource/list")
    suspend fun favDetail(
        @Query("media_id") id: Long,
        @Query("pn") index: Int,
        @Query("ps") size: Int
    ): BaseResponse<FavResourceListResponse>


    /**
     * 用户粉丝
     */
    @GET("x/relation/followers")
    suspend fun userFollowers(
        @Query("vmid") userId: Long,
        @Query("pn") index: Int = 1,
        @Query("ps") size: Int = 20
    ): BaseResponse<UserListResponse>

    @GET("x/relation/followings")
    suspend fun userFollowings(
        @Query("vmid") userId: Long,
        @Query("pn") index: Int = 1,
        @Query("ps") size: Int = 20
    ): BaseResponse<UserListResponse>

    @GET("x/space/navnum")
    suspend fun userNavNum(@Query("mid") id: Long)


    @GET("x/polymer/web-space/home/seasons_series")
    suspend fun seasonsSeriesList(
        @Query("mid") userId: Long,
        @Query("page_num") index: Int = 1,
        @Query("page_size") size: Int = 20
    ): BaseResponse<SeasonsListDataResponse>

    /**
     * 获取用户投稿的视频
     */
    @GET("x/series/recArchivesByKeywords")
    suspend fun userVideos(
        @Query("mid") userId: Long,
        @Query("ps") size: Int,
        @Query("pn") index: Int,
        @Query("keywords") keywords: String = ""
    ): BaseResponse<UserArchivesResponse>

    @GET
    suspend fun request(@Url url: String)

    @GET("x/web-interface/wbi/search/type")
    suspend fun search(
        @QueryMap params: Map<String, String>
    ): BaseResponse<SearchListResponse>

    /**
     * 退出登录
     */
    @POST("https://passport.bilibili.com/login/exit/v2")
    suspend fun logout(): BaseResponse<EmptyJson>

    @FormUrlEncoded
    @POST("x/v2/history/report")
    suspend fun reportHistory(
        @Field("aid") aid: Long,
        @Field("cid") cid: Long,
        @Field("progress") progress: Long,
        @Field("platform") platform: String = "android",
        @Field("csrf") csrf: String
    )

    @GET("x/player/pagelist")
    suspend fun pageList(
        @Query("bvid") bvid: String
    ): BaseResponse<List<PageResponse>>

    companion object {
        const val baseUrl = "https://api.bilibili.com/"
    }
}