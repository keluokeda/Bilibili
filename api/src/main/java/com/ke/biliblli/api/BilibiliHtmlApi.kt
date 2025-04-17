package com.ke.biliblli.api

import retrofit2.http.GET


interface BilibiliHtmlApi {

    @GET("https://space.bilibili.com/1/dynamic")
    suspend fun dynamic(): String

    @GET("https://www.bilibili.com/")
    suspend fun mainPage()
}