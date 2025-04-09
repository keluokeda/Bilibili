package com.ke.biliblli.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("comment_entity")
data class CommentEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo("comment_id")
    val commentId: Long,
    val oid: Long,
    val type: Int,
    val username: String,
    val avatar: String,
    /**
     * 父评论id，一级评论为0
     */
    val parent: Long,
    val liked: Boolean,
    val like: Long,
    val date: Long,
    val ip: String,
    @ColumnInfo("time_desc")
    val timeDesc: String,
    val content: String
)
