package com.saiful.shared.di

import com.saiful.shared.data.api.MultiSearchApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MultiSearchModule {

    @Provides
    @Singleton
    fun provideMultiSearchApiService(retrofit: Retrofit): MultiSearchApiService =
        retrofit.create(MultiSearchApiService::class.java)
}
