@file:OptIn(ExperimentalSerializationApi::class)

package com.ke.biliblli.api.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class CommentResponse(
    val page: CommentPage,
    val replies: List<CommentReply>? = null
)

@Serializable
data class CommentPage(
    val num: Int,
    val size: Int,
    val count: Int
)

@Serializable
data class CommentReply(
    val rpid: Long,
    val oid: Long,
    val type: Int,
    val mid: Long,
    val parent: Long,
    val count: Int,
    val state: Int,
    val ctime: Long,
    val like: Int,
    val action: Int,
    val member: CommentOwner,
    val content: CommentContent,
    @JsonNames("reply_control")
    val replyControl: CommentReplyControl
)

@Serializable
data class CommentOwner(
    val avatar: String,
    @JsonNames("uname")
    val name: String,
    val mid: String,
    val sex: String
)

@Serializable
data class CommentContent(
    val message: String,
    @JsonNames("max_line")
    val maxLine: Int
)

@Serializable
data class CommentReplyControl(
    val location: String = "",
    @JsonNames("time_desc")
    val timeDesc: String = "",
    val action: Int = 0
)
