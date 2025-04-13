@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class VideoInfoResponse(
    @JsonNames("View")
    val view: VideoDetailResponse,
    @JsonNames("Card")
    val card: UpInfo,
    @JsonNames("Tags")
    val tags: List<VideoTag>,
    @JsonNames("Related")
    val related: List<VideoDetailResponse>
)

@Serializable
data class UpInfo(
    val card: UpInfoCard,
    val following: Boolean,
    val follower: Long,
    /**
     * 稿件
     */
    @JsonNames("archive_count")
    val archiveCount: Long,
    /**
     * 专栏
     */
    @JsonNames("article_count")
    val articleCount: Long
)

@Serializable
data class UpInfoCard(
    val mid: Long,
    val name: String,
    val face: String,
    /**
     * 粉丝
     */
    val fans: Long,
    /**
     * 关注
     */
    val friend: Long,
    val attention: Long,
    val sign: String,
//    @JsonNames("level_info")
//    val levelInfo: UserLevelInfo,
    val pendant: AvatarPendant,
    val nameplate: Nameplate
)

@Serializable
data class Nameplate(
    val nid: Long,
    val name: String,
    val image: String,
    @JsonNames("image_small")
    val smallImage: String,
    val level: String,
    val condition: String
)

@Serializable
data class VideoTag(
    @JsonNames("tag_id")
    val id: Long,
    @JsonNames("tag_name")
    val name: String,
    @JsonNames("tag_type")
    val type: String,
    @JsonNames("jump_url")
    val url: String
)