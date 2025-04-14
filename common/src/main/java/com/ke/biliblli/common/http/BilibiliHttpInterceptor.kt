package com.ke.biliblli.common.http

import okhttp3.Interceptor
import okhttp3.Response

class BilibiliHttpInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("env", "prod")
            .addHeader("app-key", "android64")
            .addHeader("x-bili-aurora-zone", "sh001")
            .addHeader("referer", "https://www.bilibili.com/")
            .method(original.method, original.body).build()

        val response = chain.proceed(request)

        return response
    }
}