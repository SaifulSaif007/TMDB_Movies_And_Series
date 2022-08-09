package com.saiful.movie.data.repository

import com.saiful.movie.data.api.MovieApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailsRepo @Inject constructor(private val movieApiService: MovieApiService){

    suspend fun movieDetails(id: Int) =  movieApiService.movieDetails(id)
}