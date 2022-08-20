package com.saiful.movie.data.repository

import com.saiful.movie.data.api.MovieApiService
import javax.inject.Inject

class CollectionRepo
@Inject constructor(private val apiService: MovieApiService) {

    suspend fun movieCollection(id: Int) = apiService.collections(id)
}