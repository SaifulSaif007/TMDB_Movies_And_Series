package com.saiful.movie.view.dashboard

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.DashboardRepo
import com.saiful.movie.model.NowPlayingMovies
import com.saiful.movie.model.PopularMovies
import com.saiful.movie.model.TopRatedMoves
import com.saiful.movie.model.UpcomingMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardVM
@Inject constructor(private val dashboardRepo: DashboardRepo) : BaseOpsViewModel() {

    var popularMoviesList = MutableStateFlow<PopularMovies?>(null)
    var nowPlayingMoviesList = MutableStateFlow<NowPlayingMovies?>(null)
    var topRatedMoviesList = MutableStateFlow<TopRatedMoves?>(null)
    var upcomingMoviesList = MutableStateFlow<UpcomingMovies?>(null)

    init {
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

    private fun fetchUpcomingMovies(){
        executeRestCodeBlock(upcomingMovie){
            dashboardRepo.getUpcomingMovies(1)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when(operationTag){
            popularMovie -> {
                when(val response = data as GenericResponse<*>){
                    is BaseResponse.Success ->  {
                        popularMoviesList.value = response.body as PopularMovies
                    }
                    else -> {}
                }
            }
            nowPlayingMovie -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        nowPlayingMoviesList.value = response.body as NowPlayingMovies
                    }
                    else -> {}
                }
            }
            topRatedMovie -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        topRatedMoviesList.value = response.body as TopRatedMoves
                    }
                    else -> {}
                }
            }
            upcomingMovie -> {
                when(val response = data as GenericResponse<*>){
                    is BaseResponse.Success -> {
                        upcomingMoviesList.value = response.body as UpcomingMovies
                    }
                    else -> {}
                }
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