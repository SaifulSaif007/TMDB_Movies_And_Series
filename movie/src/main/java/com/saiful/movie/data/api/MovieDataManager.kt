package com.saiful.movie.data.api

import com.saiful.base.network.RestApiService

object MovieDataManager {

    private const val baseUrl = "https://api.themoviedb.org/3/"

    fun apiService() : MovieApiService {
        return RestApiService.generate(MovieApiService::class.java, baseUrl)
    }
}