package com.ke.biliblli.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.ke.biliblli.db.entity.HomeRecommendVideoEntity

@Dao
interface HomeRecommendVideoDao {
    @Insert
    suspend fun insertAll(list: List<HomeRecommendVideoEntity>)
}