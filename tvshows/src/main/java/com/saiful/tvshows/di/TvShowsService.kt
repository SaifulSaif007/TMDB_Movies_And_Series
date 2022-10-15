package com.saiful.tvshows.di

import com.saiful.base.util.navigation.TvShowModuleNavigation
import com.saiful.tvshows.data.api.TvShowsApiService
import com.saiful.tvshows.navigation.ShowNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TvShowsService {

    @Provides
    @Singleton
    fun provideTvShowsApi(retrofit: Retrofit): TvShowsApiService =
        retrofit.create(TvShowsApiService::class.java)

    @Provides
    @Singleton
    fun provideNavigation(): TvShowModuleNavigation = ShowNavigationImpl()

}