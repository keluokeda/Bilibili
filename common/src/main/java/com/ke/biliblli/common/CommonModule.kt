package com.ke.biliblli.common

import android.content.Context
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.ke.biliblli.api.BilibiliApi
import com.ke.biliblli.common.http.BilibiliHttpInterceptor
import com.ke.biliblli.common.http.BilibiliProtoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.CookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.protobuf.ProtoConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideCookieJar(@ApplicationContext context: Context): CookieJar {


        return PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }

    @Provides
    @Singleton
    fun provideProtoApi(cookieJar: CookieJar): BilibiliProtoApi {
        val client = OkHttpClient
            .Builder()
            .cookieJar(cookieJar)
            .addInterceptor(BilibiliHttpInterceptor())
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BilibiliApi.baseUrl)
            .addConverterFactory(
                ProtoConverterFactory.create()
            ).build()
            .create(BilibiliProtoApi::class.java).apply {
//                GlobalScope.launch {
//                    val response = dm(1, 1176840, 1)
//                    val item = response.getElems(0)
//
//                    Log.d("GlobalScope", "${response.elemsCount} $item")
//                }
            }
    }

    @Provides
    @Singleton
    fun provideApi(cookieJar: CookieJar): BilibiliApi {


        val logger = HttpLoggingInterceptor {
            Log.d("Http", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient
            .Builder()
            .cookieJar(cookieJar)
            .addInterceptor(BilibiliHttpInterceptor())
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

