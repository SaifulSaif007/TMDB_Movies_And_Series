package com.saiful.movie.data.repository

import com.saiful.movie.data.api.MovieApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepo
@Inject constructor(private val apiService: MovieApiService) {

    suspend fun getPopularMovies() = apiService.popularMovies(1)

}