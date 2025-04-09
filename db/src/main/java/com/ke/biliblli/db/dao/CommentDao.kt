package com.ke.biliblli.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ke.biliblli.db.entity.CommentEntity

@Dao
interface CommentDao {
    @Insert
    suspend fun insertAll(list: List<CommentEntity>)

    @Query("select * from comment_entity where oid = :oid and type = :type")
    fun getComments(
        oid: Long,
        type: Int
    ): PagingSource<Int, CommentEntity>

    @Query("delete from comment_entity where oid = :oid and type = :type")
    suspend fun deleteAll( oid: Long,
                           type: Int)
}