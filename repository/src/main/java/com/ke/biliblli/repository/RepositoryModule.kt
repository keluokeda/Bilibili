package com.ke.biliblli.repository

import com.ke.biliblli.common.BilibiliRepository
import com.ke.biliblli.common.BilibiliStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideBilibiliRepository(impl: BilibiliRepositoryImpl): BilibiliRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideStorage(impl: BilibiliStorageImpl): BilibiliStorage = impl
}