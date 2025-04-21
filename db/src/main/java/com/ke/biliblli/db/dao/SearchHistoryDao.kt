package com.ke.biliblli.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.biliblli.db.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        entity: SearchHistoryEntity
    )

    /**
     * 查询所有
     */
    @Query("select * from search_history_entity order by time desc")
    fun all(): Flow<List<SearchHistoryEntity>>
}