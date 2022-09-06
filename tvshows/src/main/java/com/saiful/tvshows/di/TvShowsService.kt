package com.saiful.tvshows.di

import com.saiful.tvshows.data.api.TvShowsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TvShowsService {

    @Provides
    @Singleton
    fun provideTvShowsApi(retrofit: Retrofit): TvShowsApiService =
        retrofit.create(TvShowsApiService::class.java)
}