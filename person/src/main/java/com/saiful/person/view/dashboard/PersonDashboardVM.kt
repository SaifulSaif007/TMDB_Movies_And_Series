package com.saiful.person.view.dashboard

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.person.data.repository.DashboardRepo
import com.saiful.person.model.PersonResponse
import com.saiful.shared.model.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PersonDashboardUiState(
    val popularPersons: List<Person> = emptyList(),
    val trendingPersons: List<Person> = emptyList()
)

@HiltViewModel
class PersonDashboardVM @Inject constructor(
    private val repo: DashboardRepo
) : BaseOpsViewModel() {

    private val _uiState = MutableStateFlow(PersonDashboardUiState())
    val uiState = _uiState.asStateFlow()

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
        val response = (data as? GenericResponse<*>)?.let {
            if (it is BaseResponse.Success) it.body as? PersonResponse else null
        } ?: return

        _uiState.update { currentState ->
            when (operationTag) {
                popularPersons -> currentState.copy(popularPersons = response.results)
                trendingPersons -> currentState.copy(trendingPersons = response.results)
                else -> currentState
            }
        }
    }

    private companion object {
        const val popularPersons = "POPULAR_PERSON"
        const val trendingPersons = "TRENDING_PERSON"
    }
}