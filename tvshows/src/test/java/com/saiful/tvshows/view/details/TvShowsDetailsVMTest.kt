package com.saiful.tvshows.view.details

import org.mockito.kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.base.session.SessionManager
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.WatchProvidersResponse
import com.saiful.shared.model.account.AccountActionResponse
import com.saiful.shared.model.account.AccountStateResponse
import com.saiful.shared.model.account.RatedValue
import com.saiful.tvshows.data.repository.ShowDetailsRepo
import com.saiful.tvshows.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TvShowsDetailsVMTest : BaseViewModelTest() {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: ShowDetailsRepo = mock()
    private val accountRepository: AccountRepository = mock()
    private val sessionManager: SessionManager = mock()

    private val apiError = BaseResponse.ApiError(
        errorBody = GenericError(
            status_code = "400",
            status_message = "Failed to process request"
        ),
        code = 400
    )

    private lateinit var tvShowDetails: TvShowDetails
    private lateinit var tvShowCastResponse: TvShowCastResponse
    private lateinit var tvShowsResponse: TvShowsResponse
    private lateinit var viewModel: TvShowsDetailsVM

    private val showId = 1

    override fun setup() {
        tvShowDetails = TvShowDetails()
        tvShowCastResponse = TvShowCastResponse(
            cast = listOf()
        )
        tvShowsResponse =
            TvShowsResponse(
                page = 1,
                results = listOf(TvShows(id = 1)),
                totalPages = 1,
                totalResults = 1
            )
        whenever(sessionManager.isLoggedIn).thenReturn(flowOf(false))
    }

    override fun tearDown() {
        reset(repository, accountRepository, sessionManager)
    }

    private fun initViewModel() {
        viewModel = TvShowsDetailsVM(
            repo = repository,
            accountRepository = accountRepository,
            sessionManager = sessionManager,
            dispatcherProvider = mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `verify fetch show details is successful`() {
        runTest {
            whenever(repository.showDetails(showId)).thenReturn(
                BaseResponse.Success(tvShowDetails)
            )
            whenever(repository.showCasts(showId)).thenReturn(
                BaseResponse.Success(tvShowCastResponse)
            )
            whenever(repository.showRecommendation(showId)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )
            whenever(repository.similarShows(showId)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )
            whenever(repository.watchProviders(showId)).thenReturn(
                BaseResponse.Success(WatchProvidersResponse(id = showId, results = emptyMap()))
            )

            initViewModel()

            viewModel.fetchShowDetails(showId)

            verify(repository, times(1)).showDetails(any())
            verify(repository, times(1)).showCasts(any())
            verify(repository, times(1)).showRecommendation(any())
            verify(repository, times(1)).similarShows(any())
            verify(repository, times(1)).watchProviders(any())

            assert(viewModel.uiState.value.showDetails != null)
            assert(viewModel.uiState.value.showCasts != null)
            assert(viewModel.uiState.value.recommendations?.totalPages == 1)
            assert(viewModel.uiState.value.similarShows?.page == 1)
            assert(viewModel.uiState.value.watchProviders != null)
        }
    }

    @Test
    fun `verify fetchAccountStates updates state when logged in`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(sessionManager.currentSessionId).thenReturn("active_session_tv_123")
            whenever(accountRepository.getTvShowAccountStates(showId)).thenReturn(
                BaseResponse.Success(
                    AccountStateResponse(
                        id = showId,
                        favorite = false,
                        watchlist = true,
                        rated = RatedValue(7.0f)
                    )
                )
            )

            initViewModel()
            viewModel.fetchAccountStates(showId)

            assert(!viewModel.uiState.value.isFavorite)
            assert(viewModel.uiState.value.isWatchlisted)
            assert(viewModel.uiState.value.userRating == 7.0f)
        }
    }

    @Test
    fun `verify submitShowRating updates userRating optimistically and succeeds`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(accountRepository.rateTvShow(showId, 8.5f)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )

            initViewModel()
            viewModel.submitShowRating(showId, 8.5f)

            assert(viewModel.uiState.value.userRating == 8.5f)
            verify(accountRepository, times(1)).rateTvShow(showId, 8.5f)
        }
    }

    @Test
    fun `verify submitShowRating reverts on api error`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(accountRepository.rateTvShow(showId, 8.5f)).thenReturn(apiError)

            initViewModel()
            viewModel.submitShowRating(showId, 8.5f)

            assert(viewModel.uiState.value.userRating == null)
            verify(accountRepository, times(1)).rateTvShow(showId, 8.5f)
        }
    }

    @Test
    fun `verify deleteShowRating clears userRating and succeeds`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(accountRepository.rateTvShow(showId, 6.0f)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )
            whenever(accountRepository.deleteTvShowRating(showId)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )

            initViewModel()
            viewModel.submitShowRating(showId, 6.0f)
            assert(viewModel.uiState.value.userRating == 6.0f)

            viewModel.deleteShowRating(showId)
            assert(viewModel.uiState.value.userRating == null)
            verify(accountRepository, times(1)).deleteTvShowRating(showId)
        }
    }

    @Test
    fun `verify toggleFavorite toggles to true optimistically and calls markFavorite with tv type`() {
        runTest(mainCoroutineRule.testDispatcher) {
            val userSession = com.saiful.base.model.UserSession(
                sessionId = "test_session",
                accountId = 12345,
                username = "testuser",
                name = "Test User",
                avatarPath = null,
                gravatarHash = null,
                includeAdult = false
            )
            val sessionStateFlow = kotlinx.coroutines.flow.MutableStateFlow<com.saiful.base.model.UserSession?>(userSession)
            whenever(sessionManager.sessionFlow).thenReturn(sessionStateFlow)
            whenever(accountRepository.markFavorite(12345, "tv", showId, true)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )

            initViewModel()
            assert(!viewModel.uiState.value.isFavorite)

            viewModel.toggleFavorite(showId)

            assert(viewModel.uiState.value.isFavorite)
            verify(accountRepository, times(1)).markFavorite(12345, "tv", showId, true)
        }
    }

    @Test
    fun `verify toggleFavorite reverts state when markFavorite returns error`() {
        runTest(mainCoroutineRule.testDispatcher) {
            val userSession = com.saiful.base.model.UserSession(
                sessionId = "test_session",
                accountId = 12345,
                username = "testuser",
                name = "Test User",
                avatarPath = null,
                gravatarHash = null,
                includeAdult = false
            )
            val sessionStateFlow = kotlinx.coroutines.flow.MutableStateFlow<com.saiful.base.model.UserSession?>(userSession)
            whenever(sessionManager.sessionFlow).thenReturn(sessionStateFlow)
            whenever(accountRepository.markFavorite(12345, "tv", showId, true)).thenReturn(apiError)

            initViewModel()
            viewModel.toggleFavorite(showId)

            assert(!viewModel.uiState.value.isFavorite)
            verify(accountRepository, times(1)).markFavorite(12345, "tv", showId, true)
        }
    }

    @Test
    fun `verify toggleWatchlist toggles to true optimistically and calls markWatchlist with tv type`() {
        runTest(mainCoroutineRule.testDispatcher) {
            val userSession = com.saiful.base.model.UserSession(
                sessionId = "test_session",
                accountId = 12345,
                username = "testuser",
                name = "Test User",
                avatarPath = null,
                gravatarHash = null,
                includeAdult = false
            )
            val sessionStateFlow = kotlinx.coroutines.flow.MutableStateFlow<com.saiful.base.model.UserSession?>(userSession)
            whenever(sessionManager.sessionFlow).thenReturn(sessionStateFlow)
            whenever(accountRepository.markWatchlist(12345, "tv", showId, true)).thenReturn(
                BaseResponse.Success(AccountActionResponse(statusCode = 1, statusMessage = "Success"))
            )

            initViewModel()
            assert(!viewModel.uiState.value.isWatchlisted)

            viewModel.toggleWatchlist(showId)

            assert(viewModel.uiState.value.isWatchlisted)
            verify(accountRepository, times(1)).markWatchlist(12345, "tv", showId, true)
        }
    }

    @Test
    fun `verify toggleWatchlist reverts state when markWatchlist returns error`() {
        runTest(mainCoroutineRule.testDispatcher) {
            val userSession = com.saiful.base.model.UserSession(
                sessionId = "test_session",
                accountId = 12345,
                username = "testuser",
                name = "Test User",
                avatarPath = null,
                gravatarHash = null,
                includeAdult = false
            )
            val sessionStateFlow = kotlinx.coroutines.flow.MutableStateFlow<com.saiful.base.model.UserSession?>(userSession)
            whenever(sessionManager.sessionFlow).thenReturn(sessionStateFlow)
            whenever(accountRepository.markWatchlist(12345, "tv", showId, true)).thenReturn(apiError)

            initViewModel()
            viewModel.toggleWatchlist(showId)

            assert(!viewModel.uiState.value.isWatchlisted)
            verify(accountRepository, times(1)).markWatchlist(12345, "tv", showId, true)
        }
    }

    @Test
    fun `verify selectWatchCountry updates state`() {
        runTest(mainCoroutineRule.testDispatcher) {
            initViewModel()
            viewModel.selectWatchCountry("CA")
            assert(viewModel.uiState.value.selectedWatchCountry == "CA")
        }
    }
}
