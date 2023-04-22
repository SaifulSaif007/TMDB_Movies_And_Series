package com.saiful.movie.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.MovieDetailsRepo
import com.saiful.movie.model.MovieCastResponse
import com.saiful.movie.model.MovieDetailsResponse
import com.saiful.movie.model.MoviesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieDetailsVM
@Inject constructor(private val repo: MovieDetailsRepo) : BaseOpsViewModel() {

    val movieDetailsResponse = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieCast = MutableStateFlow<MovieCastResponse?>(null)
    val recommendation = MutableStateFlow<MoviesResponse?>(null)
    val similar = MutableStateFlow<MoviesResponse?>(null)

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
        when (operationTag) {
            movie_details -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        movieDetailsResponse.value = data.body as MovieDetailsResponse
                    }
                    else -> {}
                }
            }
            movie_cast -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        movieCast.value = data.body as MovieCastResponse
                    }
                    else -> {}
                }
            }
            movie_recommendation -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        recommendation.value = data.body as MoviesResponse
                    }
                    else -> {}
                }
            }
            movie_similar -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        similar.value = data.body as MoviesResponse
                    }
                    else -> {}
                }
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