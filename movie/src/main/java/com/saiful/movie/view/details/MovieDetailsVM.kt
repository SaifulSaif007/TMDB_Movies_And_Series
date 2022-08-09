package com.saiful.movie.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.MovieDetailsRepo
import com.saiful.movie.model.MovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieDetailsVM
@Inject constructor(private val repo: MovieDetailsRepo): BaseOpsViewModel() {

    var movieDetails = MutableStateFlow<MovieDetails?>(null)

    fun fetchMovieDetails(id: Int){
        executeRestCodeBlock(movie_details){
            repo.movieDetails(id)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        if (operationTag == movie_details) {
            when (val response = data as GenericResponse<*>) {
                is BaseResponse.Success -> {
                    movieDetails.value = data.body as MovieDetails
                }
                else -> {}
            }
        }
    }

    private companion object {
        const val movie_details = "MOVIE_DETAILS"
    }
}