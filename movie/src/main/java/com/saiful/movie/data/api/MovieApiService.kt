package com.saiful.movie.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.movie.model.*
import retrofit2.http.*

interface MovieApiService {

    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("page") page: Int
    ): GenericResponse<MoviesResponse>

    @GET("movie/now_playing")
    suspend fun nowPlayingMovies(
        @Query("page") page: Int
    ): GenericResponse<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun topRatedMovies(
        @Query("page") page: Int
    ): GenericResponse<MoviesResponse>

    @GET("movie/upcoming")
    suspend fun upcomingMovies(
        @Query("page") page: Int
    ): GenericResponse<MoviesResponse>

    @GET("movie/{id}")
    suspend fun movieDetails(
        @Path("id") id: Int,
        @Query("append_to_response") res: String = "videos"
    ): GenericResponse<MovieDetailsResponse>

    @GET("movie/{id}/credits")
    suspend fun movieCast(
        @Path("id") id: Int,
    ): GenericResponse<MovieCastResponse>

    @GET("movie/{id}/recommendations")
    suspend fun recommendation(
        @Path("id") id: Int,
        @Query("page") page: Int = 1
    ): GenericResponse<MoviesResponse>

    @GET("movie/{id}/similar")
    suspend fun similarMovie(
        @Path("id") id: Int,
        @Query("page") page: Int = 1
    ): GenericResponse<MoviesResponse>

    @GET("collection/{id}")
    suspend fun collections(
        @Path("id") id: Int
    ): GenericResponse<MovieCollection>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): GenericResponse<MoviesResponse>
}