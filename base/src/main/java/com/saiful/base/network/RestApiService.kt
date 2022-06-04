package com.saiful.base.network

import com.deshipay.base.network.ThrowableAdapter
import com.saiful.base.BuildConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RestApiService {

    private val loggingInterceptor: HttpLoggingInterceptor = if(BuildConfig.DEBUG) {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .addInterceptor(UserInterceptor())
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()

    private val moshi = Moshi.Builder()
        .add(ThrowableAdapter())
        .build()

    private fun createRetrofit(baseUrl: String) = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addCallAdapterFactory(ResponseAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    fun <S> generate(serviceClass: Class<S>, serviceBaseURL: String): S {
        return createRetrofit(serviceBaseURL).create(serviceClass)
    }
}
