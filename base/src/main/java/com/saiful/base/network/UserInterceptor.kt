package com.saiful.base.network

import com.saiful.base.BuildConfig
import com.saiful.base.session.SessionManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = BuildConfig.ACCESS_TOKEN
        val originalUrl = chain.request().url

        val urlBuilder: HttpUrl.Builder = originalUrl.newBuilder()
            .addQueryParameter("api_key", token)

        val activeSessionId = sessionManager.currentSessionId
        if (!activeSessionId.isNullOrBlank() && originalUrl.queryParameter("session_id") == null) {
            urlBuilder.addQueryParameter("session_id", activeSessionId)
        }

        val url = urlBuilder.build()

        val userAgentRequest = chain.request()
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .url(url)
            .build()

        return chain.proceed(userAgentRequest)
    }
}
