package com.saiful.shared.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.model.discover.DiscoverMoviesResponse
import com.saiful.shared.model.discover.DiscoverTvShowsResponse
import com.saiful.shared.model.discover.GenresResponse
import com.saiful.shared.model.discover.WatchProvidersListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApiService {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") withGenres: String? = null,
        @Query("primary_release_year") primaryReleaseYear: Int? = null,
        @Query("with_watch_providers") withWatchProviders: String? = null,
        @Query("watch_region") watchRegion: String = "US",
        @Query("vote_count.gte") voteCountGte: Int? = null
    ): GenericResponse<DiscoverMoviesResponse>

    @GET("discover/tv")
    suspend fun discoverTvShows(
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") withGenres: String? = null,
        @Query("first_air_date_year") firstAirDateYear: Int? = null,
        @Query("with_watch_providers") withWatchProviders: String? = null,
        @Query("watch_region") watchRegion: String = "US",
        @Query("vote_count.gte") voteCountGte: Int? = null
    ): GenericResponse<DiscoverTvShowsResponse>

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): GenericResponse<GenresResponse>

    @GET("genre/tv/list")
    suspend fun getTvGenres(): GenericResponse<GenresResponse>

    @GET("watch/providers/movie")
    suspend fun getMovieWatchProviders(
        @Query("watch_region") watchRegion: String = "US"
    ): GenericResponse<WatchProvidersListResponse>

    @GET("watch/providers/tv")
    suspend fun getTvWatchProviders(
        @Query("watch_region") watchRegion: String = "US"
    ): GenericResponse<WatchProvidersListResponse>
}
