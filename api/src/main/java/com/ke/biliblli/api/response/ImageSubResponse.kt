@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames



@Serializable
data class WbiImage(
    @JsonNames("img_url")
    val image: String,
    @JsonNames("sub_url")
    val sub: String
)