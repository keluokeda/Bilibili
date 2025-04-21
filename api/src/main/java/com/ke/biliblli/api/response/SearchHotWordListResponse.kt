@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class SearchHotWordListResponse(
    val list: List<SearchHotWord>
)

@Serializable
data class SearchHotWord(
    val keyword: String,
    @JsonNames("show_name")
    val showName: String
)