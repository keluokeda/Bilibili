package com.ke.biliblli.common

import com.ke.biliblli.api.response.BaseResponse
import com.ke.biliblli.api.response.CommentResponse
import com.ke.biliblli.api.response.DynamicResponse
import com.ke.biliblli.api.response.DynamicUpListResponse
import com.ke.biliblli.api.response.FavResourceListResponse
import com.ke.biliblli.api.response.HistoryResponse
import com.ke.biliblli.api.response.HomeRecommendListResponse
import com.ke.biliblli.api.response.LaterWatchResponse
import com.ke.biliblli.api.response.LoginInfoResponse
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
import com.ke.biliblli.common.entity.BilibiliDanmaku
import com.ke.biliblli.common.http.BilibiliProtoApi

interface BilibiliRepository {

    val bilibiliProtoApi: BilibiliProtoApi

    suspend fun homeRecommendVideos(index: Int): BaseResponse<HomeRecommendListResponse>

//    suspend fun getBuvid3(): String?

    suspend fun favDetail(
        id: Long,
        index: Int,
        size: Int
    ): BaseResponse<FavResourceListResponse>

    suspend fun userFollowers(
        userId: Long,
        index: Int = 1,
        size: Int = 20
    ): BaseResponse<UserListResponse>

    suspend fun userFollowings(
        userId: Long,
        index: Int = 1,
        size: Int = 20
    ): BaseResponse<UserListResponse>

    suspend fun userNavNum(id: Long)


    suspend fun userFav(
        mid: Long
    ): BaseResponse<UserFavListResponse>

    suspend fun userRelationStatus(uid: Long): BaseResponse<RelationStatusResponse>

    suspend fun userInfo(
        mid: Long,
    ): BaseResponse<UserInfoResponse>

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

    suspend fun loginInfo(): BaseResponse<LoginInfoResponse>


    suspend fun initBuvid()

    suspend fun videoInfo(
        bvid: String
    ): BaseResponse<VideoInfoResponse>


    suspend fun laterWatchList(): BaseResponse<LaterWatchResponse>

    suspend fun comments(index: Int, oid: Long, type: Int, sort: Int): BaseResponse<CommentResponse>


    suspend fun dynamicList(
        offset: String? = null,
        type: String = "all",
        mid: Long?
    ): BaseResponse<DynamicResponse>

    suspend fun updateDynamicUpList(): BaseResponse<DynamicUpListResponse>


    /**
     * 弹幕列表
     */
    suspend fun danmakuList(type: Int, id: Long, index: Int): List<BilibiliDanmaku> {
        val response = bilibiliProtoApi.dm(type, id, index)
        return response.elemsList.map {
            BilibiliDanmaku(
                id = it.id,
                progress = it.progress,
                mode = it.mode,
                fontSize = it.fontsize,
                color = it.color,
                content = it.content
            )
        }
    }


    suspend fun logout()


    suspend fun search(keywords: String, index: Int): BaseResponse<SearchListResponse>

    suspend fun userVideos(
        userId: Long, index: Int, size: Int,
        keywords: String = ""
    ): BaseResponse<UserArchivesResponse>

    suspend fun seasonsSeriesList(
        userId: Long,
        index: Int = 1,
        size: Int = 20
    ): BaseResponse<SeasonsListDataResponse>
}