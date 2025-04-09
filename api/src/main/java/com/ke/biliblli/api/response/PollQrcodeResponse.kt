package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class PollQrcodeResponse(
    val url: String,
    @JsonNames("refresh_token")
    val refreshToken: String,
    val timestamp: Long,
    val code: Int,
    val message: String
) {
    val success: Boolean
        get() = code == 0
}
