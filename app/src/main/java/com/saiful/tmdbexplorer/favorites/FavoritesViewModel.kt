package com.saiful.tmdbexplorer.favorites

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.shared.data.repository.paging.FavoriteMoviesPagingSource
import com.saiful.shared.data.repository.paging.FavoriteTvShowsPagingSource
import com.saiful.base.session.SessionManager
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.account.AccountMediaTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {

    private val _selectedTab = MutableStateFlow(AccountMediaTab.MOVIES)
    val selectedTab: StateFlow<AccountMediaTab> = _selectedTab.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = sessionManager.sessionFlow
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = sessionManager.sessionFlow.value != null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteMovies: Flow<PagingData<Movies>> = sessionManager.sessionFlow
        .flatMapLatest { session ->
            val accountId = session?.accountId
            if (accountId != null) {
                Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
                    FavoriteMoviesPagingSource(accountRepository, accountId)
                }.flow
            } else {
                flowOf(PagingData.empty())
            }
        }.cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteTvShows: Flow<PagingData<TvShows>> = sessionManager.sessionFlow
        .flatMapLatest { session ->
            val accountId = session?.accountId
            if (accountId != null) {
                Pager(PagingConfig(pageSize = 20, enablePlaceholders = false)) {
                    FavoriteTvShowsPagingSource(accountRepository, accountId)
                }.flow
            } else {
                flowOf(PagingData.empty())
            }
        }.cachedIn(viewModelScope)

    fun selectTab(tab: AccountMediaTab) {
        _selectedTab.update { tab }
    }
}
