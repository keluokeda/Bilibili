package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class LaterWatchResponse(
    val count: Int,
    val list: List<LaterWatchVideo>
)

@Serializable
data class LaterWatchVideo(
    val aid: Long,
    val cid: Long,
    val bvid: String,
    val pic: String,
    val title: String,
    val owner: VideoOwner,
    val stat: VideoStatus,
    val duration: Long
)
