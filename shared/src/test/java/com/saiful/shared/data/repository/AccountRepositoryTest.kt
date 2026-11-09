package com.saiful.shared.data.repository

import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.shared.data.api.AccountApiService
import com.saiful.shared.model.account.AccountActionResponse
import com.saiful.shared.model.account.AccountMediaItem
import com.saiful.shared.model.account.AccountMediaListResponse
import com.saiful.shared.model.account.AccountStateResponse
import com.saiful.shared.model.account.FavoriteRequest
import com.saiful.shared.model.account.RatingRequest
import com.saiful.shared.model.account.RatedValue
import com.saiful.shared.model.account.WatchlistRequest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever

class AccountRepositoryTest : BaseRepositoryTest() {

    private val accountApiService: AccountApiService = mock()
    private lateinit var accountRepository: AccountRepository

    private val accountId = 123
    private val movieId = 456
    private val tvId = 789
    private val page = 1
    private val rating = 7.5f

    private val successAction = AccountActionResponse(statusCode = 1, statusMessage = "Success")

    private val mediaListResponse = AccountMediaListResponse(
        page = 1,
        results = listOf(
            AccountMediaItem(id = 1, title = "Movie 1", posterPath = "/path.jpg")
        ),
        totalPages = 1,
        totalResults = 1
    )

    private val accountStateResponse = AccountStateResponse(
        id = movieId,
        favorite = true,
        watchlist = false,
        rated = RatedValue(value = 7.5f)
    )

    @Before
    override fun setup() {
        accountRepository = AccountRepository(accountApiService)
    }

    @After
    override fun tearDown() {
        reset(accountApiService)
    }

    // ── Favorites ──────────────────────────────────────────────────────────

    @Test
    fun `verify get favorite movies returns success`() {
        runBlocking {
            whenever(accountApiService.getFavoriteMovies(accountId, page))
                .thenReturn(BaseResponse.Success(mediaListResponse))

            val result = accountRepository.getFavoriteMovies(accountId, page)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify get favorite movies returns api error`() {
        runBlocking {
            whenever(accountApiService.getFavoriteMovies(accountId, page))
                .thenReturn(apiError)

            val result = accountRepository.getFavoriteMovies(accountId, page)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify get favorite movies returns network error`() {
        runBlocking {
            whenever(accountApiService.getFavoriteMovies(accountId, page))
                .thenReturn(networkError)

            val result = accountRepository.getFavoriteMovies(accountId, page)
            assert(result is BaseResponse.NetworkError)
        }
    }

    @Test
    fun `verify get favorite tv shows returns success`() {
        runBlocking {
            whenever(accountApiService.getFavoriteTvShows(accountId, page))
                .thenReturn(BaseResponse.Success(mediaListResponse))

            val result = accountRepository.getFavoriteTvShows(accountId, page)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify get favorite tv shows returns api error`() {
        runBlocking {
            whenever(accountApiService.getFavoriteTvShows(accountId, page))
                .thenReturn(apiError)

            val result = accountRepository.getFavoriteTvShows(accountId, page)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify mark favorite movie returns success`() {
        runBlocking {
            val request = FavoriteRequest(mediaType = "movie", mediaId = movieId, favorite = true)
            whenever(accountApiService.markFavorite(accountId, request))
                .thenReturn(BaseResponse.Success(successAction))

            val result = accountRepository.markFavorite(accountId, "movie", movieId, true)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify mark favorite movie returns api error`() {
        runBlocking {
            val request = FavoriteRequest(mediaType = "movie", mediaId = movieId, favorite = true)
            whenever(accountApiService.markFavorite(accountId, request))
                .thenReturn(apiError)

            val result = accountRepository.markFavorite(accountId, "movie", movieId, true)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify remove favorite tv show returns success`() {
        runBlocking {
            val request = FavoriteRequest(mediaType = "tv", mediaId = tvId, favorite = false)
            whenever(accountApiService.markFavorite(accountId, request))
                .thenReturn(BaseResponse.Success(successAction))

            val result = accountRepository.markFavorite(accountId, "tv", tvId, false)
            assert(result is BaseResponse.Success)
        }
    }

    // ── Watchlist ───────────────────────────────────────────────────────────

    @Test
    fun `verify get watchlist movies returns success`() {
        runBlocking {
            whenever(accountApiService.getWatchlistMovies(accountId, page))
                .thenReturn(BaseResponse.Success(mediaListResponse))

            val result = accountRepository.getWatchlistMovies(accountId, page)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify get watchlist movies returns network error`() {
        runBlocking {
            whenever(accountApiService.getWatchlistMovies(accountId, page))
                .thenReturn(networkError)

            val result = accountRepository.getWatchlistMovies(accountId, page)
            assert(result is BaseResponse.NetworkError)
        }
    }

    @Test
    fun `verify get watchlist tv shows returns success`() {
        runBlocking {
            whenever(accountApiService.getWatchlistTvShows(accountId, page))
                .thenReturn(BaseResponse.Success(mediaListResponse))

            val result = accountRepository.getWatchlistTvShows(accountId, page)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify get watchlist tv shows returns api error`() {
        runBlocking {
            whenever(accountApiService.getWatchlistTvShows(accountId, page))
                .thenReturn(apiError)

            val result = accountRepository.getWatchlistTvShows(accountId, page)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify mark watchlist movie returns success`() {
        runBlocking {
            val request = WatchlistRequest(mediaType = "movie", mediaId = movieId, watchlist = true)
            whenever(accountApiService.markWatchlist(accountId, request))
                .thenReturn(BaseResponse.Success(successAction))

            val result = accountRepository.markWatchlist(accountId, "movie", movieId, true)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify mark watchlist movie returns unknown error`() {
        runBlocking {
            val request = WatchlistRequest(mediaType = "movie", mediaId = movieId, watchlist = true)
            whenever(accountApiService.markWatchlist(accountId, request))
                .thenReturn(unknownError)

            val result = accountRepository.markWatchlist(accountId, "movie", movieId, true)
            assert(result is BaseResponse.UnknownError)
        }
    }

    // ── Rating ──────────────────────────────────────────────────────────────

    @Test
    fun `verify rate movie returns success`() {
        runBlocking {
            whenever(accountApiService.rateMovie(movieId, RatingRequest(rating)))
                .thenReturn(BaseResponse.Success(successAction))

            val result = accountRepository.rateMovie(movieId, rating)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify rate movie returns api error`() {
        runBlocking {
            whenever(accountApiService.rateMovie(movieId, RatingRequest(rating)))
                .thenReturn(apiError)

            val result = accountRepository.rateMovie(movieId, rating)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify delete movie rating returns success`() {
        runBlocking {
            whenever(accountApiService.deleteMovieRating(movieId))
                .thenReturn(BaseResponse.Success(successAction))

            val result = accountRepository.deleteMovieRating(movieId)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify delete movie rating returns network error`() {
        runBlocking {
            whenever(accountApiService.deleteMovieRating(movieId))
                .thenReturn(networkError)

            val result = accountRepository.deleteMovieRating(movieId)
            assert(result is BaseResponse.NetworkError)
        }
    }

    @Test
    fun `verify rate tv show returns success`() {
        runBlocking {
            whenever(accountApiService.rateTvShow(tvId, RatingRequest(rating)))
                .thenReturn(BaseResponse.Success(successAction))

            val result = accountRepository.rateTvShow(tvId, rating)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify rate tv show returns api error`() {
        runBlocking {
            whenever(accountApiService.rateTvShow(tvId, RatingRequest(rating)))
                .thenReturn(apiError)

            val result = accountRepository.rateTvShow(tvId, rating)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify delete tv show rating returns success`() {
        runBlocking {
            whenever(accountApiService.deleteTvShowRating(tvId))
                .thenReturn(BaseResponse.Success(successAction))

            val result = accountRepository.deleteTvShowRating(tvId)
            assert(result is BaseResponse.Success)
        }
    }

    @Test
    fun `verify delete tv show rating returns unknown error`() {
        runBlocking {
            whenever(accountApiService.deleteTvShowRating(tvId))
                .thenReturn(unknownError)

            val result = accountRepository.deleteTvShowRating(tvId)
            assert(result is BaseResponse.UnknownError)
        }
    }

    // ── Account State ───────────────────────────────────────────────────────

    @Test
    fun `verify get movie account states returns success with favorite and rating`() {
        runBlocking {
            whenever(accountApiService.getMovieAccountStates(movieId))
                .thenReturn(BaseResponse.Success(accountStateResponse))

            val result = accountRepository.getMovieAccountStates(movieId)
            assert(result is BaseResponse.Success)
            val body = (result as BaseResponse.Success).body
            assert(body.favorite)
            assert(!body.watchlist)
            assert(body.rated?.value == 7.5f)
        }
    }

    @Test
    fun `verify get movie account states returns api error`() {
        runBlocking {
            whenever(accountApiService.getMovieAccountStates(movieId))
                .thenReturn(apiError)

            val result = accountRepository.getMovieAccountStates(movieId)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify get tv show account states returns success`() {
        runBlocking {
            val tvState = AccountStateResponse(id = tvId, favorite = false, watchlist = true, rated = null)
            whenever(accountApiService.getTvShowAccountStates(tvId))
                .thenReturn(BaseResponse.Success(tvState))

            val result = accountRepository.getTvShowAccountStates(tvId)
            assert(result is BaseResponse.Success)
            val body = (result as BaseResponse.Success).body
            assert(!body.favorite)
            assert(body.watchlist)
            assert(body.rated == null)
        }
    }

    @Test
    fun `verify get tv show account states returns network error`() {
        runBlocking {
            whenever(accountApiService.getTvShowAccountStates(tvId))
                .thenReturn(networkError)

            val result = accountRepository.getTvShowAccountStates(tvId)
            assert(result is BaseResponse.NetworkError)
        }
    }
}