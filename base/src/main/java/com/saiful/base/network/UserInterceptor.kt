package com.saiful.base.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class UserInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl = chain.request().url.newBuilder()
            .addQueryParameter("api_key", "697bf3a9a65fafc6982838746d30694b").build()

        val userAgentRequest = chain.request()
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .url(url)
            .build()

        return chain.proceed(userAgentRequest)
    }
}
