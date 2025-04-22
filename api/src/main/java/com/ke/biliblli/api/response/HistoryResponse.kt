@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class HistoryResponse(
    val cursor: HistoryCursor? = null,
    val list: List<HistoryItem> = emptyList()
)

@Serializable
data class HistoryCursor(
    val max: Long,
    @JsonNames("view_at")
    val at: Long,
    val business: String
)

@Serializable
data class HistoryItem(
    val title: String,
    val cover: String,
    val history: History,
    val duration: Long,
    val progress: Long,
    @JsonNames("author_name")
    val authorName: String
)

@Serializable
data class History(
    val bvid: String
)
