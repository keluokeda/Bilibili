@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class DynamicUpListResponse(
    @JsonNames("up_list")
    val upList: DynamicUpList
)

@Serializable
data class DynamicUpList(
    val items: List<DynamicUpItem>
)

@Serializable
data class DynamicUpItem(
    val face: String,
    val mid: Long,
    @JsonNames("has_update")
    val hasUpdate: Boolean,
    val uname: String
)
