package com.saiful.movie.data.repository

import com.saiful.movie.data.api.MovieApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepo
@Inject constructor(private val apiService: MovieApiService) {

    suspend fun getPopularMovies(pageNo: Int) = apiService.popularMovies(pageNo)

    suspend fun getNowPlayingMovies(pageNo: Int) = apiService.nowPlayingMovies(pageNo)

    suspend fun getTopRatedMovies(pageNo: Int) = apiService.topRatedMovies(pageNo)

    suspend fun getUpcomingMovies(pageNo: Int) = apiService.upcomingMovies(pageNo)
}