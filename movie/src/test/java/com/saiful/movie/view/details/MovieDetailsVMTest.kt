package com.saiful.movie.view.details

import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.base.session.SessionManager
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.movie.data.repository.MovieDetailsRepo
import com.saiful.movie.model.DateRange
import com.saiful.movie.model.MovieCastResponse
import com.saiful.movie.model.MovieDetailsResponse
import com.saiful.movie.model.MoviesResponse
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.shared.model.account.AccountActionResponse
import com.saiful.shared.model.account.AccountStateResponse
import com.saiful.shared.model.account.RatedValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MovieDetailsVMTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

    private val repository: MovieDetailsRepo = mock()
    private val accountRepository: AccountRepository = mock()
    private val sessionManager: SessionManager = mock()

    private val apiError = BaseResponse.ApiError(
        errorBody = GenericError(
            status_code = "400",
            status_message = "Failed to process request"
        ),
        code = 400
    )

    private lateinit var movieDetailsResponse: MovieDetailsResponse
    private lateinit var movieCastResponse: MovieCastResponse
    private lateinit var moviesResponse: MoviesResponse

    private lateinit var viewModel: MovieDetailsVM

    override fun setup() {
        movieDetailsResponse = MovieDetailsResponse(
            adult = false
        )
        movieCastResponse = MovieCastResponse(
            id = 1,
            cast = emptyList()
        )
        moviesResponse = MoviesResponse(
            dates = DateRange(maximum = "", minimum = ""),
            page = 1,
            results = emptyList(),
            totalPages = 2,
            totalResults = 2
        )
        whenever(sessionManager.isLoggedIn).thenReturn(flowOf(false))
    }

    override fun tearDown() {
        reset(repository, accountRepository, sessionManager)
    }

    private fun initViewModel() {
        viewModel = MovieDetailsVM(
            repo = repository,
            accountRepository = accountRepository,
            sessionManager = sessionManager,
            dispatcherProvider = coroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `verify movie details data fetch successful`() {
        runTest(coroutineRule.testDispatcher) {
            whenever(repository.movieDetails(1)).thenReturn(
                BaseResponse.Success(movieDetailsResponse)
            )
            whenever(repository.movieCasts(1)).thenReturn(
                BaseResponse.Success(movieCastResponse)
            )
            whenever(repository.similarMovie(1)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.recommendation(1)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )

            initViewModel()

            viewModel.fetchMovieDetails(1)

            verify(repository, times(1)).movieDetails(1)
            verify(repository, times(1)).movieCasts(1)
            verify(repository, times(1)).similarMovie(1)
            verify(repository, times(1)).recommendation(1)

            assert(viewModel.uiState.value.movieDetails?.adult == false)
            assert(viewModel.uiState.value.cast.isNotEmpty() || viewModel.uiState.value.movieDetails != null)
        }
    }

    @Test
    fun `verify fetchAccountStates updates state when logged in`() {
        runTest(coroutineRule.testDispatcher) {
            whenever(sessionManager.currentSessionId).thenReturn("active_session_123")
            whenever(accountRepository.getMovieAccountStates(1)).thenReturn(
                BaseResponse.Success(
                    AccountStateResponse(
                        id = 1,
                        favorite = true,
                        watchlist = false,
                        rated = RatedValue(8.5f)
                    )
                )
            )

            initViewModel()
            viewModel.fetchAccountStates(1)

            assert(viewModel.uiState.value.isFavorite)
            assert(!viewModel.uiState.value.isWatchlisted)
            assert(viewModel.uiState.value.userRating == 8.5f)
        }
    }

    @Test
    fun `verify submitMovieRating updates userRating optimistically and succeeds`() {
        runTest(coroutineRule.testDispatcher) {
            whenever(accountRepository.rateMovie(1, 9.0f)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )

            initViewModel()
            viewModel.submitMovieRating(1, 9.0f)

            assert(viewModel.uiState.value.userRating == 9.0f)
            verify(accountRepository, times(1)).rateMovie(1, 9.0f)
        }
    }

    @Test
    fun `verify submitMovieRating reverts on api error`() {
        runTest(coroutineRule.testDispatcher) {
            whenever(accountRepository.rateMovie(1, 9.0f)).thenReturn(apiError)

            initViewModel()
            viewModel.submitMovieRating(1, 9.0f)

            assert(viewModel.uiState.value.userRating == null)
            verify(accountRepository, times(1)).rateMovie(1, 9.0f)
        }
    }

    @Test
    fun `verify deleteMovieRating clears userRating and succeeds`() {
        runTest(coroutineRule.testDispatcher) {
            whenever(accountRepository.rateMovie(1, 8.0f)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )
            whenever(accountRepository.deleteMovieRating(1)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )

            initViewModel()
            viewModel.submitMovieRating(1, 8.0f)
            assert(viewModel.uiState.value.userRating == 8.0f)

            viewModel.deleteMovieRating(1)
            assert(viewModel.uiState.value.userRating == null)
            verify(accountRepository, times(1)).deleteMovieRating(1)
        }
    }
}
