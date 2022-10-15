package com.saiful.movie.di

import com.saiful.base.util.navigation.MovieModuleNavigation
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.navigation.MovieNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieService {

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApiService =
        retrofit.create(MovieApiService::class.java)

    @Provides
    @Singleton
    fun provideNavigation(): MovieModuleNavigation = MovieNavigationImpl()

}