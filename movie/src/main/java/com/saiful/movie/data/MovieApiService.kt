package com.saiful.movie.data

import com.saiful.base.network.model.GenericResponse
import com.saiful.movie.model.PopularMovies
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("page") page: Int
    ): GenericResponse<PopularMovies>

}