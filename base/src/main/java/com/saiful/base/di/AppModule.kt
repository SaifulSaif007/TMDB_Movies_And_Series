package com.saiful.base.di

import com.saiful.base.BuildConfig
import com.saiful.base.network.ResponseAdapterFactory
import com.saiful.base.network.ThrowableAdapter
import com.saiful.base.network.UserInterceptor
import com.saiful.base.util.AppConstants.baseUrl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val loggingInterceptor: HttpLoggingInterceptor = if (BuildConfig.DEBUG) {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    @Singleton
    fun httpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .addInterceptor(UserInterceptor())
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder()
        .add(ThrowableAdapter())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(ResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}