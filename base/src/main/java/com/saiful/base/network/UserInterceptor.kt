package com.saiful.base.network

import com.saiful.base.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class UserInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = BuildConfig.ACCESS_TOKEN
        val url: HttpUrl =
            chain.request().url.newBuilder().addQueryParameter("api_key", token).build()

        val userAgentRequest = chain.request()
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .url(url)
            .build()

        return chain.proceed(userAgentRequest)
    }
}
