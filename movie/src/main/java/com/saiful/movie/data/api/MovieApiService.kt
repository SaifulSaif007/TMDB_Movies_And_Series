package com.saiful.movie.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.movie.model.NowPlayingMovies
import com.saiful.movie.model.PopularMovies
import com.saiful.movie.model.TopRatedMoves
import com.saiful.movie.model.UpcomingMovies
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("page") page: Int
    ): GenericResponse<PopularMovies>

    @GET("movie/now_playing")
    suspend fun nowPlayingMovies(
        @Query("page") page: Int
    ) : GenericResponse<NowPlayingMovies>

    @GET("movie/top_rated")
    suspend fun topRatedMovies(
        @Query("page") page: Int
    ) : GenericResponse<TopRatedMoves>

    @GET("movie/upcoming")
    suspend fun upcomingMovies(
        @Query("page") page: Int
    ) : GenericResponse<UpcomingMovies>
}