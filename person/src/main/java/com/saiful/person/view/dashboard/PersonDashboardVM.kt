package com.saiful.person.view.dashboard

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.person.data.repository.DashboardRepo
import com.saiful.person.model.PersonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PersonDashboardVM
@Inject constructor(private val repo: DashboardRepo) : BaseOpsViewModel() {

    val popularPersonList = MutableStateFlow<PersonResponse?>(null)
    val trendingPersonList = MutableStateFlow<PersonResponse?>(null)

    init {
        popularPerson()
        trendingPerson()
    }

    private fun popularPerson() {
        executeRestCodeBlock(popularPersons) {
            repo.popularPersons()
        }
    }

    private fun trendingPerson() {
        executeRestCodeBlock(trendingPersons) {
            repo.trendingPersons()
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            popularPersons -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        popularPersonList.value = response.body as PersonResponse
                    }
                    else -> {}
                }
            }
            trendingPersons -> {
                when (val response = data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        trendingPersonList.value = response.body as PersonResponse
                    }
                    else -> {}
                }
            }
        }
    }


    private companion object {
        const val popularPersons = "POPULAR_PERSON"
        const val trendingPersons = "TRENDING_PERSON"
    }

}