package com.saiful.person.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.person.data.repository.PersonDetailsRepo
import com.saiful.person.model.PersonDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PersonDetailsVM
@Inject constructor(private val repo: PersonDetailsRepo) : BaseOpsViewModel() {

    val personDetails = MutableStateFlow<PersonDetails?>(null)

    fun fetchPersonDetails(personId: Int) {
        executeRestCodeBlock(person_details) {
            repo.personDetails(personId)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            person_details -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        personDetails.value = data.body as PersonDetails
                    }
                    else -> {}
                }
            }
        }
    }


    private companion object {
        const val person_details = "PERSON_DETAILS"
    }

}