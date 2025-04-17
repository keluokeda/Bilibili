package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class UserFavListResponse(
    val count: Int,
    val list: List<UserFavResponse>
)

@Serializable
data class UserFavResponse(
    val id: Long,
    val fid: Long,
    val mid: Long,
    val title: String
)
