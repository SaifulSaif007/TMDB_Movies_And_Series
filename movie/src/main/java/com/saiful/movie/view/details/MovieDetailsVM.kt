package com.saiful.movie.view.details

import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.session.SessionManager
import com.saiful.base.util.DefaultDispatcherProvider
import com.saiful.base.util.DispatcherProvider
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.MovieDetailsRepo
import com.saiful.movie.model.Cast
import com.saiful.movie.model.MovieCastResponse
import com.saiful.movie.model.MovieDetailsResponse
import com.saiful.movie.model.MoviesResponse
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.shared.model.Movies
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

data class MovieDetailsUiState(
    val movieDetails: MovieDetailsResponse? = null,
    val cast: List<Cast> = emptyList(),
    val recommendations: List<MoviesResponse> = emptyList(),
    val recommendationsList: List<Movies> = emptyList(),
    val similarMoviesList: List<Movies> = emptyList(),
    val isFavorite: Boolean = false,
    val isWatchlisted: Boolean = false,
    val userRating: Float? = null,
    val isAccountStateLoading: Boolean = false
)

@HiltViewModel
class MovieDetailsVM @Inject constructor(
    private val repo: MovieDetailsRepo,
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : BaseOpsViewModel(dispatcherProvider) {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = sessionManager.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun fetchMovieDetails(id: Int) {
        executeRestCodeBlock(movie_details) {
            repo.movieDetails(id)
        }
        executeRestCodeBlock(movie_cast) {
            repo.movieCasts(id)
        }
        executeRestCodeBlock(movie_recommendation) {
            repo.recommendation(id)
        }
        executeRestCodeBlock(movie_similar) {
            repo.similarMovie(id)
        }
        fetchAccountStates(id)
    }

    fun fetchAccountStates(movieId: Int) {
        viewModelScope.launch(dispatcherProvider.main()) {
            val sessionId = sessionManager.currentSessionId
            if (!sessionId.isNullOrBlank()) {
                _uiState.update { it.copy(isAccountStateLoading = true) }
                val result = withContext(dispatcherProvider.io()) {
                    accountRepository.getMovieAccountStates(movieId)
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

    fun submitMovieRating(movieId: Int, rating: Float) {
        val previousRating = _uiState.value.userRating
        _uiState.update { it.copy(userRating = rating) }

        viewModelScope.launch(dispatcherProvider.main()) {
            val result = withContext(dispatcherProvider.io()) {
                accountRepository.rateMovie(movieId, rating)
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

    fun deleteMovieRating(movieId: Int) {
        val previousRating = _uiState.value.userRating
        _uiState.update { it.copy(userRating = null) }

        viewModelScope.launch(dispatcherProvider.main()) {
            val result = withContext(dispatcherProvider.io()) {
                accountRepository.deleteMovieRating(movieId)
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

    fun toggleFavorite(movieId: Int) {
        val userSession = sessionManager.sessionFlow.value
        val accountId = userSession?.accountId
        if (accountId == null) {
            emitMessage("Please log in to add to favorites")
            return
        }

        val previousState = _uiState.value.isFavorite
        val targetState = !previousState
        _uiState.update { it.copy(isFavorite = targetState) }

        viewModelScope.launch(dispatcherProvider.main()) {
            val result = withContext(dispatcherProvider.io()) {
                accountRepository.markFavorite(accountId, "movie", movieId, targetState)
            }
            when (result) {
                is BaseResponse.Success -> {
                    emitMessage(if (targetState) "Added to Favorites" else "Removed from Favorites")
                }
                is BaseResponse.ApiError -> {
                    _uiState.update { it.copy(isFavorite = previousState) }
                    emitMessage(result.errorBody.status_message)
                }
                else -> {
                    _uiState.update { it.copy(isFavorite = previousState) }
                    emitMessage("Failed to update favorites")
                }
            }
        }
    }

    fun toggleWatchlist(movieId: Int) {
        val userSession = sessionManager.sessionFlow.value
        val accountId = userSession?.accountId
        if (accountId == null) {
            emitMessage("Please log in to add to watchlist")
            return
        }

        val previousState = _uiState.value.isWatchlisted
        val targetState = !previousState
        _uiState.update { it.copy(isWatchlisted = targetState) }

        viewModelScope.launch(dispatcherProvider.main()) {
            val result = withContext(dispatcherProvider.io()) {
                accountRepository.markWatchlist(accountId, "movie", movieId, targetState)
            }
            when (result) {
                is BaseResponse.Success -> {
                    emitMessage(if (targetState) "Added to Watchlist" else "Removed from Watchlist")
                }
                is BaseResponse.ApiError -> {
                    _uiState.update { it.copy(isWatchlisted = previousState) }
                    emitMessage(result.errorBody.status_message)
                }
                else -> {
                    _uiState.update { it.copy(isWatchlisted = previousState) }
                    emitMessage("Failed to update watchlist")
                }
            }
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        _uiState.update { currentState ->
            when (operationTag) {
                movie_details -> currentState.copy(
                    movieDetails = data.body as? MovieDetailsResponse
                )
                movie_cast -> currentState.copy(
                    cast = (data.body as? MovieCastResponse)?.cast ?: emptyList()
                )
                movie_recommendation -> currentState.copy(
                    recommendationsList = (data.body as? MoviesResponse)?.results ?: emptyList()
                )
                movie_similar -> currentState.copy(
                    similarMoviesList = (data.body as? MoviesResponse)?.results ?: emptyList()
                )
                else -> currentState
            }
        }
    }

    private companion object {
        const val movie_details = "MOVIE_DETAILS"
        const val movie_cast = "MOVIE_CAST"
        const val movie_recommendation = "MOVIE_RECOMMENDATION"
        const val movie_similar = "MOVIE_SIMILAR"
    }
}
