package com.saiful.person.view.details

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.person.data.repository.PersonDetailsRepo
import com.saiful.person.model.MovieCredits
import com.saiful.person.model.PersonDetails
import com.saiful.person.model.PersonImage
import com.saiful.person.model.TvShowsCredits
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PersonDetailsUiState(
    val personDetails: PersonDetails? = null,
    val personImageList: PersonImage? = null,
    val personMovieList: MovieCredits? = null,
    val personShowsList: TvShowsCredits? = null
)

@HiltViewModel
class PersonDetailsVM @Inject constructor(
    private val repo: PersonDetailsRepo
) : BaseOpsViewModel() {

    private val _uiState = MutableStateFlow(PersonDetailsUiState())
    val uiState = _uiState.asStateFlow()

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
        executeRestCodeBlock(person_shows_credits) {
            repo.personTvShowsCredits(personId)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            person_details -> {
                _uiState.update { it.copy(personDetails = data.body as PersonDetails) }
            }
            person_image -> {
                _uiState.update { it.copy(personImageList = data.body as PersonImage) }
            }
            person_movie_credits -> {
                _uiState.update { it.copy(personMovieList = data.body as MovieCredits) }
            }
            person_shows_credits -> {
                _uiState.update { it.copy(personShowsList = data.body as TvShowsCredits) }
            }
        }
    }

    private companion object {
        const val person_details = "PERSON_DETAILS"
        const val person_image = "PERSON_IMAGE"
        const val person_movie_credits = "MOVIE_CREDITS"
        const val person_shows_credits = "SHOWS_CREDITS"
    }
}