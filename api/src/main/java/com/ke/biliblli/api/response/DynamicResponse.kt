@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames

@Serializable
data class DynamicResponse(
    @JsonNames("has_more")
    val more: Boolean,
    @JsonNames("items")
    val list: List<DynamicItem>,
    val offset: String?
)

@Serializable
data class DynamicItem(
    @JsonNames("id_str")
    val id: String? = null,
    val type: String,
    val visible: Boolean,
    @JsonNames("modules")
    val module: DynamicItemModule,
    val orig: DynamicItem? = null
)

@Serializable
data class DynamicItemModule(
    @JsonNames("module_author")
    val author: DynamicItemModuleAuthor,
    @JsonNames("module_dynamic")
    val dynamic: DynamicItemModuleDynamic
)

@Serializable
data class DynamicItemModuleAuthor(
    val face: String,
    val following: Boolean? = null,
    val name: String,
    val mid: Long,
    @JsonNames("pub_action")
    val action: String,
    @JsonNames("pub_time")
    val time: String
)

@Serializable
data class DynamicItemModuleDynamic(
    val major: DynamicItemModuleDynamicMajor? = null,
    val desc: DynamicItemModuleDynamicDesc? = null
)

@Serializable
data class DynamicItemModuleDynamicDesc(
    val text: String
)

@Serializable
data class DynamicItemModuleDynamicMajor(
    val type: String,
    val archive: DynamicItemModuleDynamicMajorArchive? = null,
    @JsonNames("live_rcmd")
    val live: DynamicItemModuleDynamicMajorLive? = null,
    val draw: DynamicItemModuleDynamicMajorDraw? = null,
    val article: DynamicItemModuleDynamicMajorArticle? = null,
    val pgc: DynamicItemModuleDynamicMajorPgc? = null
)

private val liveJson = Json {
    ignoreUnknownKeys = true
}

@Serializable
data class DynamicItemModuleDynamicMajorLive(
    val content: String
) {
    fun json() =
        liveJson.decodeFromString<DynamicItemModuleDynamicMajorLiveJson>(content).info

}

@Serializable
data class DynamicItemModuleDynamicMajorLiveJson(
    @JsonNames("live_play_info")
    val info: DynamicItemModuleDynamicMajorLiveJsonInfo
)

@Serializable
data class DynamicItemModuleDynamicMajorLiveJsonInfo(
    @JsonNames("live_id")
    val id: Long,
    val title: String,
    val cover: String
)

@Serializable
data class DynamicItemModuleDynamicMajorArchive(
    val aid: String,
    val bvid: String,
    @JsonNames("duration_text")
    val duration: String,
    val title: String,
    val cover: String
)

@Serializable
data class DynamicItemModuleDynamicMajorDraw(
    val id: Long,
    val items: List<ImageSrc>
)

@Serializable
data class ImageSrc(
    val src: String
)

@Serializable
data class DynamicItemModuleDynamicMajorArticle(
    val covers: List<String>,
    val id: Long,
    val title: String,
    val desc: String
)

@Serializable
data class DynamicItemModuleDynamicMajorPgc(
    val cover: String,
    val title: String,
    val epid: Long,
    @JsonNames("season_id")
    val seasonId: Long,
    val type: Int
)