@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class FavResourceListResponse(

    val medias: List<FavResourceMediaResponse>,
    @JsonNames("has_more")
    val more: Boolean
)

@Serializable
data class FavResourceMediaResponse(
    val id: Long,
    val title: String,
    val type: Int,
    val cover: String,
    val bvid: String
)
