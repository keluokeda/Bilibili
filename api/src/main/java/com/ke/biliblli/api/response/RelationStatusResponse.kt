package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class RelationStatusResponse(
    val mid: Long,
    val following: Int,
    val whisper: Int,
    val black: Int,
    val follower: Int
)
