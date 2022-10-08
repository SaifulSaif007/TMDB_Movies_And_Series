package com.saiful.person.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.person.data.repository.PersonDetailsRepo
import com.saiful.person.model.MovieCredits
import com.saiful.person.model.PersonDetails
import com.saiful.person.model.PersonImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PersonDetailsVM
@Inject constructor(private val repo: PersonDetailsRepo) : BaseOpsViewModel() {

    val personDetails = MutableStateFlow<PersonDetails?>(null)
    val personImageList = MutableStateFlow<PersonImage?>(null)
    val personMovieList = MutableStateFlow<MovieCredits?>(null)

    fun fetchPersonDetails(personId: Int) {
        executeRestCodeBlock(person_details) {
            repo.personDetails(personId)
        }
        executeRestCodeBlock(person_image) {
            repo.personImages(personId)
        }
        executeRestCodeBlock(person_movie_credits) {
            repo.personMovieCredits(personId)
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
            person_image -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        personImageList.value = data.body as PersonImage
                    }
                    else -> {}
                }
            }
            person_movie_credits -> {
                when (data as GenericResponse<*>) {
                    is BaseResponse.Success -> {
                        personMovieList.value = data.body as MovieCredits
                    }
                    else -> {}
                }
            }
        }
    }


    private companion object {
        const val person_details = "PERSON_DETAILS"
        const val person_image = "PERSON_IMAGE"
        const val person_movie_credits = "MOVIE_CREDITS"
    }

}