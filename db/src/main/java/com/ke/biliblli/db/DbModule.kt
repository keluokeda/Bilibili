package com.ke.biliblli.db

import android.content.Context
import androidx.room.Room
import com.ke.biliblli.db.dao.CommentDao
import com.ke.biliblli.db.dao.HomeRecommendVideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BilibiliDatabase {
        return Room
            .inMemoryDatabaseBuilder(context, BilibiliDatabase::class.java)
            .build()
    }

    @Provides
    @Singleton
    fun homeRecommendVideoDao(database: BilibiliDatabase): HomeRecommendVideoDao =
        database.homeRecommendVideoDao()

    @Provides
    @Singleton
    fun provideCommentDao(database: BilibiliDatabase): CommentDao = database.commentDao()
}