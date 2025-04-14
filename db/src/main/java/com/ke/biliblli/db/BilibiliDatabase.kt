package com.ke.biliblli.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ke.biliblli.db.dao.CommentDao
import com.ke.biliblli.db.dao.HomeRecommendVideoDao
import com.ke.biliblli.db.entity.CommentEntity
import com.ke.biliblli.db.entity.HomeRecommendVideoEntity

@Database(
    entities = [HomeRecommendVideoEntity::class, CommentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BilibiliDatabase : RoomDatabase() {

    abstract fun homeRecommendVideoDao(): HomeRecommendVideoDao

    abstract fun commentDao(): CommentDao
}