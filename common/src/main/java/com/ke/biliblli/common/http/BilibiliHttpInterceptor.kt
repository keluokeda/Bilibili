package com.ke.biliblli.common.http

import com.ke.biliblli.common.BilibiliStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BilibiliHttpInterceptor @Inject constructor(
    private val bilibiliStorage: BilibiliStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("env", "prod")
            .addHeader("app-key", "android64")
            .addHeader("x-bili-aurora-zone", "sh001")
            .addHeader("referer", "https://www.bilibili.com/")
//            .addHeader(
//                "User-Agent",
//                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.2 Safari/605.1.15"
//            )
            .addHeader(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
            )
            .method(original.method, original.body)


        val response = chain.proceed(request.build())

        return response
    }
}