@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class FavResourceListResponse(

    val medias: List<FavResourceMediaResponse>? = null,
    @JsonNames("has_more")
    val more: Boolean
)

@Serializable
data class FavResourceMediaResponse(
    val id: Long,
    val title: String,
    val type: Int,
    val cover: String,
    val bvid: String,
    val upper: FavUpper,
    @JsonNames("cnt_info")
    val info: FavResourceMediaInfo,
    val duration: Long
)

@Serializable
data class FavResourceMediaInfo(
    val play: Long,
    val danmaku: Long
)

@Serializable
data class FavUpper(
    val name: String
)
