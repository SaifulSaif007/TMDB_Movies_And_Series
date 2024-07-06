package com.saiful.tvshows.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.tvshows.data.repository.ShowDetailsRepo
import com.saiful.tvshows.model.*
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
        executeRestCodeBlock(SHOW_DETAILS) {
            repo.showDetails(showId)
        }
    }

    fun fetchShowCasts(showId: Int) {
        executeRestCodeBlock(SHOW_CAST) {
            repo.showCasts(showId)
        }
    }

    fun fetchShowRecommendation(showId: Int) {
        executeRestCodeBlock(SHOW_RECOMMENDATION) {
            repo.showRecommendation(showId)
        }
    }

    fun fetchSimilarShow(showId: Int) {
        executeRestCodeBlock(SIMILAR_SHOW) {
            repo.similarShows(showId)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            SHOW_DETAILS -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        showDetails.value = response.body as TvShowDetails
                    }

                    else -> {}
                }
            }

            SHOW_CAST -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        showCasts.value = response.body as TvShowCastResponse
                    }

                    else -> {}
                }
            }

            SHOW_RECOMMENDATION -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        recommendation.value = response.body as TvShowsResponse
                    }

                    else -> {}
                }
            }

            SIMILAR_SHOW -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        similarShow.value = response.body as TvShowsResponse
                    }

                    else -> {}
                }
            }
        }
    }

    private companion object {
        const val SHOW_DETAILS = "show_details"
        const val SHOW_CAST = "show_cast"
        const val SHOW_RECOMMENDATION  = "show_recommendation"
        const val SIMILAR_SHOW  = "similar_show"
    }
}