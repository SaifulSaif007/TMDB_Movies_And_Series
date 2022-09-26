package com.saiful.tvshows.view.season

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.tvshows.data.repository.SeasonRepo
import com.saiful.tvshows.model.SeasonDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ShowSeasonVM
@Inject constructor(private val repo: SeasonRepo) : BaseOpsViewModel() {

    val seasonDetails = MutableStateFlow<SeasonDetails?>(null)

    fun fetchSeason(showId: Int, seasonNo: Int) {
        executeRestCodeBlock(seasons) {
            repo.seasonDetails(showId, seasonNo)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        if (operationTag == seasons) {
            when (val response = data as GenericResponse<*>) {
                is BaseResponse.Success ->
                    seasonDetails.value = response.body as SeasonDetails
                else -> {}
            }
        }
    }

    private companion object {
        const val seasons = "SEASON_DETAILS"
    }
}