@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class HomeRecommendListResponse(
    val item: List<HomeRecommendResponse>
)

@Serializable
data class HomeRecommendResponse(
    val id: Long,
    val bvid: String,
    val cid: Long,
    val goto: String,
    val uri: String,
    val pic: String,
    val title: String,
    val duration: Long,
    val pubdate: Long,
    val owner: VideoOwner,
    val stat: VideoStatus,
)

@Serializable
data class VideoStatus(
    val view: Long = 0,
    val like: Long = 0,
    val danmaku: Long = 0,
    val vt: Long = 0,
    val reply: Long = 0,
    val favorite: Long = 0,
    val coin: Long = 0,
    val share: Long = 0,
    @JsonNames("now_rank")
    val nowRank: Int = 0,
    @JsonNames("his_rank")
    val historyRank: Int = 0,
    val evaluation: String = ""
)

@Serializable
data class VideoOwner(
    val mid: Long,
    val name: String,
    val face: String
)