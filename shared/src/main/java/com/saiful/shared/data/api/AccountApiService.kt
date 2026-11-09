package com.saiful.shared.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.model.account.AccountActionResponse
import com.saiful.shared.model.account.AccountMediaListResponse
import com.saiful.shared.model.account.AccountStateResponse
import com.saiful.shared.model.account.FavoriteRequest
import com.saiful.shared.model.account.RatingRequest
import com.saiful.shared.model.account.WatchlistRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountApiService {

    // ── Favorites ──────────────────────────────────────────────────────────
    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("page") page: Int = 1
    ): GenericResponse<AccountMediaListResponse>

    @GET("account/{account_id}/favorite/tv")
    suspend fun getFavoriteTvShows(
        @Path("account_id") accountId: Int,
        @Query("page") page: Int = 1
    ): GenericResponse<AccountMediaListResponse>

    @POST("account/{account_id}/favorite")
    suspend fun markFavorite(
        @Path("account_id") accountId: Int,
        @Body body: FavoriteRequest
    ): GenericResponse<AccountActionResponse>

    // ── Watchlist ───────────────────────────────────────────────────────────
    @GET("account/{account_id}/watchlist/movies")
    suspend fun getWatchlistMovies(
        @Path("account_id") accountId: Int,
        @Query("page") page: Int = 1
    ): GenericResponse<AccountMediaListResponse>

    @GET("account/{account_id}/watchlist/tv")
    suspend fun getWatchlistTvShows(
        @Path("account_id") accountId: Int,
        @Query("page") page: Int = 1
    ): GenericResponse<AccountMediaListResponse>

    @POST("account/{account_id}/watchlist")
    suspend fun markWatchlist(
        @Path("account_id") accountId: Int,
        @Body body: WatchlistRequest
    ): GenericResponse<AccountActionResponse>

    // ── Rating ──────────────────────────────────────────────────────────────
    @POST("movie/{movie_id}/rating")
    suspend fun rateMovie(
        @Path("movie_id") movieId: Int,
        @Body body: RatingRequest
    ): GenericResponse<AccountActionResponse>

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteMovieRating(
        @Path("movie_id") movieId: Int
    ): GenericResponse<AccountActionResponse>

    @POST("tv/{tv_id}/rating")
    suspend fun rateTvShow(
        @Path("tv_id") tvId: Int,
        @Body body: RatingRequest
    ): GenericResponse<AccountActionResponse>

    @DELETE("tv/{tv_id}/rating")
    suspend fun deleteTvShowRating(
        @Path("tv_id") tvId: Int
    ): GenericResponse<AccountActionResponse>

    // ── Account State ───────────────────────────────────────────────────────
    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieAccountStates(
        @Path("movie_id") movieId: Int
    ): GenericResponse<AccountStateResponse>

    @GET("tv/{tv_id}/account_states")
    suspend fun getTvShowAccountStates(
        @Path("tv_id") tvId: Int
    ): GenericResponse<AccountStateResponse>
}