package com.saiful.tvshows.view.dashboard

import androidx.lifecycle.viewModelScope
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.tvshows.data.repository.DashboardRepo
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.model.TvShowsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DashboardUiState(
    val trendingShows: List<TvShows> = emptyList(),
    val popularShows: List<TvShows> = emptyList(),
    val topRatedShows: List<TvShows> = emptyList(),
    val onAirShows: List<TvShows> = emptyList(),
    val sliderShows: List<TvShows> = emptyList()
)

@HiltViewModel
class ShowsDashboardVM @Inject constructor(
    private val repo: DashboardRepo
) : BaseOpsViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchAll()
    }

    private fun fetchAll() {
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
        val response = (data as? GenericResponse<*>)?.let {
            if (it is BaseResponse.Success) it.body as? TvShowsResponse else null
        } ?: return

        val shows = response.results
        val sliderItems = if (shows.isNotEmpty()) shows.shuffled().take(2) else emptyList()

        _uiState.update { currentState ->
            when (operationTag) {
                trendingShows -> currentState.copy(
                    trendingShows = shows,
                    sliderShows = currentState.sliderShows + sliderItems
                )
                popularShows -> currentState.copy(
                    popularShows = shows,
                    sliderShows = currentState.sliderShows + sliderItems
                )
                topRatedShows -> currentState.copy(
                    topRatedShows = shows,
                    sliderShows = currentState.sliderShows + sliderItems
                )
                onAirShows -> currentState.copy(
                    onAirShows = shows,
                    sliderShows = currentState.sliderShows + sliderItems
                )
                else -> currentState
            }
        }
    }

    private companion object {
        const val trendingShows = "TRENDING_SHOWS"
        const val popularShows = "POPULAR_SHOWS"
        const val topRatedShows = "TOP_RATED_SHOWS"
        const val onAirShows = "ON_AIR_SHOWS"
    }
}