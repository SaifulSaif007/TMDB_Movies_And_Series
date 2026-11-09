package com.saiful.shared.data.repository

import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.data.api.AccountApiService
import com.saiful.shared.model.account.AccountActionResponse
import com.saiful.shared.model.account.AccountMediaListResponse
import com.saiful.shared.model.account.AccountStateResponse
import com.saiful.shared.model.account.FavoriteRequest
import com.saiful.shared.model.account.RatingRequest
import com.saiful.shared.model.account.WatchlistRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val accountApiService: AccountApiService
) {

    // ── Favorites ──────────────────────────────────────────────────────────
    suspend fun getFavoriteMovies(
        accountId: Int,
        page: Int = 1
    ): GenericResponse<AccountMediaListResponse> =
        accountApiService.getFavoriteMovies(accountId, page)

    suspend fun getFavoriteTvShows(
        accountId: Int,
        page: Int = 1
    ): GenericResponse<AccountMediaListResponse> =
        accountApiService.getFavoriteTvShows(accountId, page)

    suspend fun markFavorite(
        accountId: Int,
        mediaType: String,
        mediaId: Int,
        favorite: Boolean
    ): GenericResponse<AccountActionResponse> =
        accountApiService.markFavorite(accountId, FavoriteRequest(mediaType, mediaId, favorite))

    // ── Watchlist ───────────────────────────────────────────────────────────
    suspend fun getWatchlistMovies(
        accountId: Int,
        page: Int = 1
    ): GenericResponse<AccountMediaListResponse> =
        accountApiService.getWatchlistMovies(accountId, page)

    suspend fun getWatchlistTvShows(
        accountId: Int,
        page: Int = 1
    ): GenericResponse<AccountMediaListResponse> =
        accountApiService.getWatchlistTvShows(accountId, page)

    suspend fun markWatchlist(
        accountId: Int,
        mediaType: String,
        mediaId: Int,
        watchlist: Boolean
    ): GenericResponse<AccountActionResponse> =
        accountApiService.markWatchlist(accountId, WatchlistRequest(mediaType, mediaId, watchlist))

    // ── Rating ──────────────────────────────────────────────────────────────
    suspend fun rateMovie(
        movieId: Int,
        rating: Float
    ): GenericResponse<AccountActionResponse> =
        accountApiService.rateMovie(movieId, RatingRequest(rating))

    suspend fun deleteMovieRating(
        movieId: Int
    ): GenericResponse<AccountActionResponse> =
        accountApiService.deleteMovieRating(movieId)

    suspend fun rateTvShow(
        tvId: Int,
        rating: Float
    ): GenericResponse<AccountActionResponse> =
        accountApiService.rateTvShow(tvId, RatingRequest(rating))

    suspend fun deleteTvShowRating(
        tvId: Int
    ): GenericResponse<AccountActionResponse> =
        accountApiService.deleteTvShowRating(tvId)

    // ── Account State ───────────────────────────────────────────────────────
    suspend fun getMovieAccountStates(
        movieId: Int
    ): GenericResponse<AccountStateResponse> =
        accountApiService.getMovieAccountStates(movieId)

    suspend fun getTvShowAccountStates(
        tvId: Int
    ): GenericResponse<AccountStateResponse> =
        accountApiService.getTvShowAccountStates(tvId)
}