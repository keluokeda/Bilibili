package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class UserListResponse(
    val list: List<UserResponse>
)

@Serializable
data class UserResponse(
    val mid: Long,
    val face: String,
    val sign: String,
    val uname: String
)
