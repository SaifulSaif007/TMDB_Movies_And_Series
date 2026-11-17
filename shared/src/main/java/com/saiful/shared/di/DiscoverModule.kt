package com.saiful.shared.di

import com.saiful.shared.data.api.DiscoverApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiscoverModule {

    @Provides
    @Singleton
    fun provideDiscoverApiService(retrofit: Retrofit): DiscoverApiService =
        retrofit.create(DiscoverApiService::class.java)
}
