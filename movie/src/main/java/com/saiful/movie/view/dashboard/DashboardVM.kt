package com.saiful.movie.view.dashboard

import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.DashboardRepo
import com.saiful.shared.model.Movies
import com.saiful.movie.model.MoviesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DashboardUiState(
    val popularMovies: List<Movies> = emptyList(),
    val nowPlayingMovies: List<Movies> = emptyList(),
    val topRatedMovies: List<Movies> = emptyList(),
    val upcomingMovies: List<Movies> = emptyList(),
    val sliderMovies: List<Movies> = emptyList()
)

@HiltViewModel
class DashboardVM @Inject constructor(
    private val dashboardRepo: DashboardRepo
) : BaseOpsViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchAll()
    }

    private fun fetchAll() {
        fetchPopularMovies()
        fetchNowPlayingMovies()
        fetchTopRatedMovies()
        fetchUpcomingMovies()
    }

    private fun fetchPopularMovies() {
        executeRestCodeBlock(popularMovie) {
            dashboardRepo.getPopularMovies(1)
        }
    }

    private fun fetchNowPlayingMovies() {
        executeRestCodeBlock(nowPlayingMovie) {
            dashboardRepo.getNowPlayingMovies(1)
        }
    }

    private fun fetchTopRatedMovies() {
        executeRestCodeBlock(topRatedMovie) {
            dashboardRepo.getTopRatedMovies(1)
        }
    }

    private fun fetchUpcomingMovies() {
        executeRestCodeBlock(upcomingMovie) {
            dashboardRepo.getUpcomingMovies(1)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        val moviesResponse = (data as? GenericResponse<*>)?.let {
            if (it is BaseResponse.Success) it.body as? MoviesResponse else null
        } ?: return

        val movies = moviesResponse.results
        val sliderItems = if (movies.isNotEmpty()) movies.shuffled().take(2) else emptyList()

        _uiState.update { currentState ->
            when (operationTag) {
                popularMovie -> currentState.copy(
                    popularMovies = movies,
                    sliderMovies = currentState.sliderMovies + sliderItems
                )
                nowPlayingMovie -> currentState.copy(
                    nowPlayingMovies = movies,
                    sliderMovies = currentState.sliderMovies + sliderItems
                )
                topRatedMovie -> currentState.copy(
                    topRatedMovies = movies,
                    sliderMovies = currentState.sliderMovies + sliderItems
                )
                upcomingMovie -> currentState.copy(
                    upcomingMovies = movies,
                    sliderMovies = currentState.sliderMovies + sliderItems
                )
                else -> currentState
            }
        }
    }

    private companion object {
        const val popularMovie = "POPULAR_MOVIE"
        const val nowPlayingMovie = "NOW_PLAYING_MOVIE"
        const val topRatedMovie = "TOP_RATED_MOVIE"
        const val upcomingMovie = "UPCOMING_MOVIE"
    }
}
