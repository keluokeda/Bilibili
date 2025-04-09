package com.ke.biliblli.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "home_recommend_video_entity")
data class HomeRecommendVideoEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val uri: String,
    val image: String,
    val duration: Long,
    @ColumnInfo("view_count")
    val viewCount: Long,
    @ColumnInfo("danmaku_count")
    val danmakuCount: Long,
    val date: Long,
    @ColumnInfo("owner_name")
    val ownerNane: String
)
