package com.saiful.tvshows.view.dashboard

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.tvshows.data.repository.DashboardRepo
import com.saiful.tvshows.model.TvShows
import com.saiful.tvshows.model.TvShowsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ShowsDashboardVM
@Inject constructor(private val repo: DashboardRepo) : BaseOpsViewModel() {

    val trendingShowsList = MutableStateFlow<TvShowsResponse?>(null)
    val popularShowsList = MutableStateFlow<TvShowsResponse?>(null)
    val topRatedShowsList = MutableStateFlow<TvShowsResponse?>(null)
    val onAirShowsList = MutableStateFlow<TvShowsResponse?>(null)
    var sliderList = arrayListOf<TvShows>()
    val sliderLoaded = MutableStateFlow<Boolean>(false)

    init {
        fetchTrendingShows()
        fetchPopularShows()
        fetchTopRatedShows()
        fetchOnAirShows()
    }

    private fun fetchTrendingShows() {
        executeRestCodeBlock(trendingShows) {
            repo.getTrendingShows(1)
        }
    }

    private fun fetchPopularShows() {
        executeRestCodeBlock(popularShows) {
            repo.getPopularShows(1)
        }
    }

    private fun fetchTopRatedShows() {
        executeRestCodeBlock(topRatedShows) {
            repo.getTopRatedShows(1)
        }
    }

    private fun fetchOnAirShows() {
        executeRestCodeBlock(onAirShows) {
            repo.getOnAirShows(1)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            trendingShows -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        trendingShowsList.value = response.body as TvShowsResponse
                        trendingShowsList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                            sliderLoaded.value = true
                        }
                    }
                    else -> {}
                }
            }
            popularShows -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        popularShowsList.value = response.body as TvShowsResponse
                        popularShowsList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                            sliderLoaded.value = true
                        }
                    }
                    else -> {}
                }
            }
            topRatedShows -> {
                when(val response = data as GenericResponse<*>){
                    is BaseResponse.Success->{
                        topRatedShowsList.value = response.body as TvShowsResponse
                        topRatedShowsList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                            sliderLoaded.value = true
                        }
                    }
                    else -> {}
                }
            }
            onAirShows ->{
                when(val response = data as GenericResponse<*>){
                    is BaseResponse.Success->{
                        onAirShowsList.value = response.body as TvShowsResponse
                        topRatedShowsList.value?.results?.shuffled()?.subList(0, 2)?.let {
                            sliderList.addAll(it)
                            sliderLoaded.value = true
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val trendingShows = "TRENDING_SHOWS"
        private const val popularShows = "POPULAR_SHOWS"
        private const val topRatedShows = "TOP_RATED_SHOWS"
        private const val onAirShows = "ON_AIR_SHOWS"
    }
}