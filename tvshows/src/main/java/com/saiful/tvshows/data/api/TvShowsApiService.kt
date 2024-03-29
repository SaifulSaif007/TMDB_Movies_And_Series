package com.saiful.tvshows.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.tvshows.model.SeasonDetails
import com.saiful.tvshows.model.TvShowCastResponse
import com.saiful.tvshows.model.TvShowDetails
import com.saiful.tvshows.model.TvShowsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowsApiService {

    @GET("trending/tv/week")
    suspend fun trendingTvShows(
        @Query("page") page: Int
    ): GenericResponse<TvShowsResponse>

    @GET("tv/popular")
    suspend fun popularTvShows(
        @Query("page") page: Int
    ): GenericResponse<TvShowsResponse>

    @GET("tv/top_rated")
    suspend fun topRatedTvShows(
        @Query("page") page: Int
    ): GenericResponse<TvShowsResponse>

    @GET("tv/on_the_air")
    suspend fun onAirTvShows(
        @Query("page") page: Int
    ): GenericResponse<TvShowsResponse>

    @GET("tv/{show_id}")
    suspend fun showDetails(
        @Path("show_id") id: Int,
        @Query("append_to_response") res: String = "videos"
    ): GenericResponse<TvShowDetails>

    @GET("tv/{show_id}/aggregate_credits")
    suspend fun showCasts(
        @Path("show_id") id: Int
    ): GenericResponse<TvShowCastResponse>

    @GET("tv/{show_id}/recommendations")
    suspend fun recommendations(
        @Path("show_id") id: Int
    ): GenericResponse<TvShowsResponse>

    @GET("tv/{show_id}/similar")
    suspend fun similarShows(
        @Path("show_id") id: Int
    ): GenericResponse<TvShowsResponse>

    @GET("tv/{show_id}}/season/{season_no}")
    suspend fun seasonDetails(
        @Path("show_id") id: Int,
        @Path("season_no") no: Int
    ): GenericResponse<SeasonDetails>

}