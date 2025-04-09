package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class VideoDetailResponse(
    val bvid: String,
    val cid: Long,
    val aid: Long,
    val videos: Int,
    val tid: Int,
//    @JsonNames("tid_v2")
//    val tidV2: Int,
    val tname: String,
//    @JsonNames("tname_v2")
//    val tnameV2: String,
    /**
     * 1：原创
     * 2：转载
     */
    val copyright: Int,
    val pic: String,
    val title: String,
    val pubdate: Long,
    val ctime: Long,
    val desc: String,
    val status: Int = 0,
    val duration: Long,
    val owner: VideoOwner,
    val stat: VideoStatus,

    )
