package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val ttl: Int = 0,
    val data: T?
) {
    val success: Boolean
        get() = code == 0
}
