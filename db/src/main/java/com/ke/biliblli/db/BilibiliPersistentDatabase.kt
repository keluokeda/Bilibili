package com.ke.biliblli.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ke.biliblli.db.dao.SearchHistoryDao
import com.ke.biliblli.db.entity.SearchHistoryEntity

@Database(
    entities = [SearchHistoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BilibiliPersistentDatabase : RoomDatabase() {

    abstract fun historyDao(): SearchHistoryDao
}