package com.ke.biliblli.common.entity

data class VideoViewEntity(
    val pic: String,
    val view: Long,
    val danmaku: Long,
    val duration: Long,
    val title: String,
    val author: String,
)