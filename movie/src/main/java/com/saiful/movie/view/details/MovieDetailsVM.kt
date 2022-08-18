package com.saiful.movie.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.MovieDetailsRepo
import com.saiful.movie.model.MovieCastResponse
import com.saiful.movie.model.MovieDetails
import com.saiful.movie.model.MoviesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieDetailsVM
@Inject constructor(private val repo: MovieDetailsRepo) : BaseOpsViewModel() {

    val movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieCast = MutableStateFlow<MovieCastResponse?>(null)
    val recommendation = MutableStateFlow<MoviesResponse?>(null)

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
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            movie_details -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        movieDetails.value = data.body as MovieDetails
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
        }
    }

    private companion object {
        const val movie_details = "MOVIE_DETAILS"
        const val movie_cast = "MOVIE_CAST"
        const val movie_recommendation = "MOVIE_RECOMMENDATION"
    }
}