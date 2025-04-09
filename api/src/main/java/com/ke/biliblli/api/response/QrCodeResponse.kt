package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class QrCodeResponse(
    val url: String,
    @JsonNames("qrcode_key")
    val key: String
)
