package com.saiful.tmdbexplorer.favorites

import androidx.lifecycle.viewModelScope
import com.saiful.base.model.UserSession
import com.saiful.base.session.SessionManager
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.shared.model.account.AccountMediaTab
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
internal class FavoritesViewModelTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

    private val accountRepository: AccountRepository = mock()
    private val sessionManager: SessionManager = mock()

    private val userSession = UserSession(
        sessionId = "test_session",
        accountId = 12345,
        username = "testuser",
        name = "Test User",
        avatarPath = null,
        gravatarHash = null,
        includeAdult = false
    )
    private val sessionFlow = MutableStateFlow<UserSession?>(null)

    private lateinit var viewModel: FavoritesViewModel

    override fun setup() {
        sessionFlow.value = null
        whenever(sessionManager.sessionFlow).thenReturn(sessionFlow)
    }

    override fun tearDown() {
        if (::viewModel.isInitialized) {
            viewModel.viewModelScope.coroutineContext.cancelChildren()
        }
        reset(accountRepository, sessionManager)
    }

    private fun initViewModel() {
        viewModel = FavoritesViewModel(
            accountRepository = accountRepository,
            sessionManager = sessionManager
        )
    }

    @Test
    fun `initial selectedTab is MOVIES`() {
        initViewModel()
        assert(viewModel.selectedTab.value == AccountMediaTab.MOVIES)
    }

    @Test
    fun `selectTab updates selectedTab correctly`() {
        initViewModel()
        viewModel.selectTab(AccountMediaTab.TV_SHOWS)
        assert(viewModel.selectedTab.value == AccountMediaTab.TV_SHOWS)

        viewModel.selectTab(AccountMediaTab.MOVIES)
        assert(viewModel.selectedTab.value == AccountMediaTab.MOVIES)
    }

    @Test
    fun `isLoggedIn reflects null session as false and valid session as true`() {
        runTest(coroutineRule.testDispatcher) {
            initViewModel()
            assert(!viewModel.isLoggedIn.value)

            sessionFlow.value = userSession
            coroutineRule.testDispatcher.advanceUntilIdle()
            assert(viewModel.isLoggedIn.value)
        }
    }

    @Test
    fun `favoriteMovies flow emits paging data when session is active`() {
        runTest(coroutineRule.testDispatcher) {
            sessionFlow.value = userSession
            initViewModel()

            val job = launch {
                viewModel.favoriteMovies.collect {}
            }
            coroutineRule.testDispatcher.advanceUntilIdle()
            job.cancel()
        }
    }

    @Test
    fun `favoriteTvShows flow emits paging data when session is active`() {
        runTest(coroutineRule.testDispatcher) {
            sessionFlow.value = userSession
            initViewModel()

            val job = launch {
                viewModel.favoriteTvShows.collect {}
            }
            coroutineRule.testDispatcher.advanceUntilIdle()
            job.cancel()
        }
    }
}
