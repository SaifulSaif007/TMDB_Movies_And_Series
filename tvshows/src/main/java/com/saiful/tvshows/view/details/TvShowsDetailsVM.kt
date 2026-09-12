package com.saiful.tvshows.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.tvshows.data.repository.ShowDetailsRepo
import com.saiful.tvshows.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class TvShowsDetailsUiState(
    val showDetails: TvShowDetails? = null,
    val showCasts: TvShowCastResponse? = null,
    val recommendations: TvShowsResponse? = null,
    val similarShows: TvShowsResponse? = null
)

@HiltViewModel
class TvShowsDetailsVM @Inject constructor(
    private val repo: ShowDetailsRepo
) : BaseOpsViewModel() {

    private val _uiState = MutableStateFlow(TvShowsDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchShowDetails(showId: Int) {
        executeRestCodeBlock(show_details) {
            repo.showDetails(showId)
        }
        executeRestCodeBlock(show_cast) {
            repo.showCasts(showId)
        }
        executeRestCodeBlock(show_recommendation) {
            repo.showRecommendation(showId)
        }
        executeRestCodeBlock(similar_show) {
            repo.similarShows(showId)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            show_details -> {
                _uiState.update { it.copy(showDetails = data.body as TvShowDetails) }
            }
            show_cast -> {
                _uiState.update { it.copy(showCasts = data.body as TvShowCastResponse) }
            }
            show_recommendation -> {
                _uiState.update { it.copy(recommendations = data.body as TvShowsResponse) }
            }
            similar_show -> {
                _uiState.update { it.copy(similarShows = data.body as TvShowsResponse) }
            }
        }
    }

    private companion object {
        const val show_details = "SHOW_DETAILS"
        const val show_cast = "SHOW_CAST"
        const val show_recommendation = "SHOW_RECOMMENDATION"
        const val similar_show = "SIMILAR_SHOW"
    }
}