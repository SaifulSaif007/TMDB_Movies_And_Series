package com.saiful.tvshows.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.tvshows.data.repository.ShowDetailsRepo
import com.saiful.tvshows.model.TvShowCastResponse
import com.saiful.tvshows.model.TvShowDetails
import com.saiful.tvshows.model.TvShowsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TvShowsDetailsVM
@Inject constructor(private val repo: ShowDetailsRepo) : BaseOpsViewModel() {

    val showDetails = MutableStateFlow<TvShowDetails?>(null)
    val showCasts = MutableStateFlow<TvShowCastResponse?>(null)
    val recommendation = MutableStateFlow<TvShowsResponse?>(null)
    val similarShow = MutableStateFlow<TvShowsResponse?>(null)

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
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        showDetails.value = data.body as TvShowDetails
                    }
                    else -> {}
                }
            }
            show_cast -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        showCasts.value = data.body as TvShowCastResponse
                    }
                    else -> {}
                }
            }
            show_recommendation -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        recommendation.value = data.body as TvShowsResponse
                    }
                    else -> {}
                }
            }
            similar_show -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        similarShow.value = data.body as TvShowsResponse
                    }
                    else -> {}
                }
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