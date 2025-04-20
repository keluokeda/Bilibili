@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class VideoUrlResponse(
    val quality: Int,
    val format: String,
    @JsonNames("timelength")
    val timeLength: Int,
    @JsonNames("accept_format")
    val acceptFormat: String,
    @JsonNames("accept_description")
    val acceptDescription: List<String>,
    @JsonNames("accept_quality")
    val acceptQuality: List<Int>,
    val dash: VideoDash,
    @JsonNames("support_formats")
    val supportFormats: List<VideoSupportFormat>,

    @JsonNames("last_play_time")
    val lastPlayTime: Long = 0
)


@Serializable
data class VideoDash(
    //秒
    val duration: Long,
    val video: List<DashVideo>,
    val audio: List<DashAudio>,
    val dolby: DashDolby? = null
)

@Serializable
data class DashVideo(
    val id: Int,
    val baseUrl: String,
    val backupUrl: List<String>,
)

@Serializable
data class DashAudio(
    val id: Int,
    val baseUrl: String,
    val backupUrl: List<String>,
)

@Serializable
data class DashDolby(
    val type: Int,
    val audio: List<DashAudio>? = null
)

@Serializable
data class VideoSupportFormat(
    val quality: Int,
    val format: String,
    @JsonNames("new_description")
    val newDescription: String,
    @JsonNames("display_desc")
    val displayDesc: String,
    val superscript: String,
    val codecs: List<String>
)
