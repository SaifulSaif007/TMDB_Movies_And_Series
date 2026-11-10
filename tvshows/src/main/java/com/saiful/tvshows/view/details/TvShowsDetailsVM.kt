package com.saiful.tvshows.view.details

import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.session.SessionManager
import com.saiful.base.util.DefaultDispatcherProvider
import com.saiful.base.util.DispatcherProvider
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.tvshows.data.repository.ShowDetailsRepo
import com.saiful.tvshows.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class TvShowsDetailsUiState(
    val showDetails: TvShowDetails? = null,
    val showCasts: TvShowCastResponse? = null,
    val recommendations: TvShowsResponse? = null,
    val similarShows: TvShowsResponse? = null,
    val isFavorite: Boolean = false,
    val isWatchlisted: Boolean = false,
    val userRating: Float? = null,
    val isAccountStateLoading: Boolean = false
)

@HiltViewModel
class TvShowsDetailsVM @Inject constructor(
    private val repo: ShowDetailsRepo,
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : BaseOpsViewModel(dispatcherProvider) {

    private val _uiState = MutableStateFlow(TvShowsDetailsUiState())
    val uiState = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = sessionManager.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun fetchShowDetails(showId: Int) {
        executeRestCodeBlock(show_details) {
            repo.showDetails(showId)
        }
        executeRestCodeBlock(show_cast) {
            repo.showCasts(showId)
        }
        executeRestCodeBlock(show_recommendation) {
            repo.showRecommendation(showId)
        }
        executeRestCodeBlock(similar_show) {
            repo.similarShows(showId)
        }
        fetchAccountStates(showId)
    }

    fun fetchAccountStates(showId: Int) {
        viewModelScope.launch(dispatcherProvider.main()) {
            val sessionId = sessionManager.currentSessionId
            if (!sessionId.isNullOrBlank()) {
                _uiState.update { it.copy(isAccountStateLoading = true) }
                val result = withContext(dispatcherProvider.io()) {
                    accountRepository.getTvShowAccountStates(showId)
                }
                when (result) {
                    is BaseResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isFavorite = result.body.favorite,
                                isWatchlisted = result.body.watchlist,
                                userRating = result.body.rated?.value,
                                isAccountStateLoading = false
                            )
                        }
                    }
                    else -> {
                        _uiState.update { it.copy(isAccountStateLoading = false) }
                    }
                }
            }
        }
    }

    fun submitShowRating(showId: Int, rating: Float) {
        val previousRating = _uiState.value.userRating
        _uiState.update { it.copy(userRating = rating) }

        viewModelScope.launch(dispatcherProvider.main()) {
            val result = withContext(dispatcherProvider.io()) {
                accountRepository.rateTvShow(showId, rating)
            }
            when (result) {
                is BaseResponse.Success -> {
                    emitMessage("Rating submitted successfully")
                }
                is BaseResponse.ApiError -> {
                    _uiState.update { it.copy(userRating = previousRating) }
                    emitMessage(result.errorBody.status_message)
                }
                else -> {
                    _uiState.update { it.copy(userRating = previousRating) }
                    emitMessage("Failed to submit rating")
                }
            }
        }
    }

    fun deleteShowRating(showId: Int) {
        val previousRating = _uiState.value.userRating
        _uiState.update { it.copy(userRating = null) }

        viewModelScope.launch(dispatcherProvider.main()) {
            val result = withContext(dispatcherProvider.io()) {
                accountRepository.deleteTvShowRating(showId)
            }
            when (result) {
                is BaseResponse.Success -> {
                    emitMessage("Rating removed")
                }
                is BaseResponse.ApiError -> {
                    _uiState.update { it.copy(userRating = previousRating) }
                    emitMessage(result.errorBody.status_message)
                }
                else -> {
                    _uiState.update { it.copy(userRating = previousRating) }
                    emitMessage("Failed to remove rating")
                }
            }
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            show_details -> {
                _uiState.update { it.copy(showDetails = data.body as TvShowDetails) }
            }
            show_cast -> {
                _uiState.update { it.copy(showCasts = data.body as TvShowCastResponse) }
            }
            show_recommendation -> {
                _uiState.update { it.copy(recommendations = data.body as TvShowsResponse) }
            }
            similar_show -> {
                _uiState.update { it.copy(similarShows = data.body as TvShowsResponse) }
            }
        }
    }

    private companion object {
        const val show_details = "SHOW_DETAILS"
        const val show_cast = "SHOW_CAST"
        const val show_recommendation = "SHOW_RECOMMENDATION"
        const val similar_show = "SIMILAR_SHOW"
    }
}
