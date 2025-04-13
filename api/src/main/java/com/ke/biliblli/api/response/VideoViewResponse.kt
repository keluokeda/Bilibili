package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable


@Serializable
data class VideoViewResponse(
    val bvid: String,
    val cid: Long,
    val pic: String
)
