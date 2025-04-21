package com.ke.biliblli.api.response

import kotlinx.serialization.Serializable

@Serializable
data class SearchSuggestResponse(
    val result: SearchSuggestTagsResponse
)

@Serializable
data class SearchSuggestTagsResponse(
    val tag: List<SearchSuggestTag>
)

@Serializable
data class SearchSuggestTag(
    val value: String
)
