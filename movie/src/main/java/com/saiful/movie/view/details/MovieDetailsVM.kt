package com.saiful.movie.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.MovieDetailsRepo
import com.saiful.movie.model.Cast
import com.saiful.movie.model.MovieCastResponse
import com.saiful.movie.model.MovieDetailsResponse
import com.saiful.movie.model.MoviesResponse
import com.saiful.shared.model.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MovieDetailsUiState(
    val movieDetails: MovieDetailsResponse? = null,
    val cast: List<Cast> = emptyList(),
    val recommendations: List<MoviesResponse> = emptyList(), // Wait, recommendation is MoviesResponse which has list of Movies
    val recommendationsList: List<Movies> = emptyList(),
    val similarMoviesList: List<Movies> = emptyList()
)

@HiltViewModel
class MovieDetailsVM @Inject constructor(
    private val repo: MovieDetailsRepo
) : BaseOpsViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState = _uiState.asStateFlow()

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
