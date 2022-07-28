package com.saiful.movie.view.dashboard

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.DashboardRepo
import com.saiful.movie.model.PopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardVM
    @Inject constructor(private val dashboardRepo: DashboardRepo) : BaseOpsViewModel() {

    var popularMoviesList = MutableStateFlow<PopularMovies?>(null)

    init {
       executeRestCodeBlock(popularMovie) {
           dashboardRepo.getPopularMovies()
       }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when(operationTag){
            popularMovie -> {
                when(val response = data as GenericResponse<PopularMovies>){
                    is BaseResponse.Success ->  {
                        popularMoviesList.value = response.body
                    }
                    else -> {}
                }
            }
        }
    }

    private companion object {
        const val popularMovie = "POPULAR_MOVIE"
    }
}