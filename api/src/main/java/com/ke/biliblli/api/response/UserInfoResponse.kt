@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class UserInfoResponse(
    val mid: Long,
    val name: String,
    val sex: String,
    val face: String,
    val sign: String,
    @JsonNames("top_photo")
    val topPhoto: String,
    val birthday: String? = null,
    val school: String? = null
)
