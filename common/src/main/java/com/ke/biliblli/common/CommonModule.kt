package com.ke.biliblli.common

import android.content.Context
import android.util.Log
import com.ke.biliblli.api.BilibiliApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    @Singleton
    fun provideApi(@ApplicationContext context: Context): BilibiliApi {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
        val logger = HttpLoggingInterceptor {
            Log.d("Http", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient
            .Builder()
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .addHeader("env", "prod")
                    .addHeader("app-key", "android64")
                    .addHeader("x-bili-aurora-zone", "sh001")
                    .addHeader("referer", "https://www.bilibili.com/")
                    .method(original.method, original.body).build()

                val response = chain.proceed(request)

                response
            }
            .addInterceptor(logger)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BilibiliApi.baseUrl)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            ).build()
            .create(BilibiliApi::class.java)
    }

}