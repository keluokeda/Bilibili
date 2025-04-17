package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class UserArchivesResponse(
    val archives: List<SeasonsArchiveResponse>
)
