package com.saiful.movie.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.movie.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("page") page: Int
    ): GenericResponse<MoviesResponse>

    @GET("movie/now_playing")
    suspend fun nowPlayingMovies(
        @Query("page") page: Int
    ) : GenericResponse<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun topRatedMovies(
        @Query("page") page: Int
    ) : GenericResponse<MoviesResponse>

    @GET("movie/upcoming")
    suspend fun upcomingMovies(
        @Query("page") page: Int
    ) : GenericResponse<MoviesResponse>

}