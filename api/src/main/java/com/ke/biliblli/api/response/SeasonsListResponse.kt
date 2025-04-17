@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
data class SeasonsListDataResponse(
    @JsonNames("items_lists")
    val itemsLists: SeasonsDataItemLists
)

@Serializable
data class SeasonsDataItemLists(

    @JsonNames("seasons_list")
    val seasonsList: List<SeasonsResponse>
)

@Serializable
data class SeasonsResponse(
    val meta: SeasonsMetaResponse,
    val archives: List<SeasonsArchiveResponse>
)

@Serializable
data class SeasonsArchiveResponse(
    val bvid: String,
    val title: String,
    val ctime: Long,
    val pic: String,
    val duration: Long
)

@Serializable
data class SeasonsMetaResponse(
    @JsonNames("season_id")
    val id: Long,
    val name: String,
    val total: Int,
    val description: String,
    val cover: String
)
