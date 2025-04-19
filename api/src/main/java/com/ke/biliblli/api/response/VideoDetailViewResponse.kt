@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class VideoDetailViewResponse(
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
    val pages: List<VideoViewPage>? = null,
    @JsonNames("ugc_season")
    val ugcSeason: VideoViewUcgSeasonList? = null
)


@Serializable
data class VideoViewPage(
    val cid: Long,
    val page: Int,
    val from: String,
    val part: String,
    val duration: Long,
    @JsonNames("first_frame")
    val firstFrame: String? = null,
    val ctime: Long
)

@Serializable
data class VideoViewUcgSeasonList(
    val id: Long,
    val title: String,
    val cover: String,
    val mid: Long,
    val sections: List<VideoViewUcgSeason>
) {
    fun allVideos() = sections.flatMap { it.episodes }

}

@Serializable
data class VideoViewUcgSeason(
    @JsonNames("season_id")
    val seasonId: Long,
    val id: Long,
    val title: String,
    val type: Int,
    val episodes: List<VideoViewUcgSeasonEpisode>
)

@Serializable
data class VideoViewUcgSeasonEpisode(
    @JsonNames("season_id")
    val seasonId: Long,
    @JsonNames("section_id")
    val sectionId: Long,
    val id: Long,
    val aid: Long,
    val cid: Long,
    val title: String,
    val attribute: Int,
    val arc: ArchiveResponse,
    val bvid: String,
)

@Serializable
data class ArchiveResponse(
    val aid: Long,
//    val bvid: String,
    val title: String,
    val ctime: Long,
    val pic: String,
    val duration: Long
)