package com.ke.biliblli.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("search_history_entity")
data class SearchHistoryEntity(
    @PrimaryKey
    val keywords: String,
    val time: Long = System.currentTimeMillis()
)
