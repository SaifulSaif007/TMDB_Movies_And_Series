package com.saiful.movie.view.dashboard

import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.DashboardRepo
import com.saiful.movie.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardVM
@Inject constructor(private val dashboardRepo: DashboardRepo) : BaseOpsViewModel() {

    val popularMoviesList = MutableStateFlow<MoviesResponse?>(null)
    val nowPlayingMoviesList = MutableStateFlow<MoviesResponse?>(null)
    val topRatedMoviesList = MutableStateFlow<MoviesResponse?>(null)
    val upcomingMoviesList = MutableStateFlow<MoviesResponse?>(null)
    val sliderList = arrayListOf<Movies>()
    val sliderLoaded = MutableStateFlow<Boolean>(false)

    init {
        fetchPopularMovies()
        fetchNowPlayingMovies()
        fetchTopRatedMovies()
        fetchUpcomingMovies()
        fetchSliderStatus()
    }

    private fun fetchSliderStatus() {
        viewModelScope.launch (Dispatchers.IO){
            combine(
                popularMoviesList,
                nowPlayingMoviesList,
                topRatedMoviesList,
                upcomingMoviesList
            ){ pop, now, top, up ->
                pop?.results != null || now?.results != null || top?.results != null || up?.results != null
            }.collectLatest {
                sliderLoaded.value = it
            }
        }
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
        when (operationTag) {
            popularMovie -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        popularMoviesList.value = response.body as MoviesResponse
                        popularMoviesList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                        }
                    }
                    else -> {}
                }
            }
            nowPlayingMovie -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        nowPlayingMoviesList.value = response.body as MoviesResponse
                        nowPlayingMoviesList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                        }
                    }
                    else -> {}
                }
            }
            topRatedMovie -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        topRatedMoviesList.value = response.body as MoviesResponse
                        topRatedMoviesList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                        }
                    }
                    else -> {}
                }
            }
            upcomingMovie -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        upcomingMoviesList.value = response.body as MoviesResponse
                        upcomingMoviesList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                        }
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