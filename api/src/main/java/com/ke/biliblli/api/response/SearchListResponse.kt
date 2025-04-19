package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class SearchListResponse(
    val result: List<SearchResponse>
)

@Serializable
data class SearchResponse(
    val bvid: String,
    val pic: String,
    val title: String,
    val pubdate: Long,
    val duration: String,
    val author: String,
    val play: Long,
    val danmaku: Long
) {
    fun duration(): Int {
        try {
            val list = duration.split(":").map {
                it.toInt()
            }

            return list[0] * 60 + list[1]
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }
}