package com.saiful.movie.data.repository

import com.saiful.movie.data.api.MovieApiService

class DashboardRepo(
    private val apiService: MovieApiService
) {

    suspend fun getPopularMovies() = apiService.popularMovies(1)
}