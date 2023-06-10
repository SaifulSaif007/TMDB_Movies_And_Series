package com.saiful.person.view.details

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.person.data.repository.PersonDetailsRepo
import com.saiful.person.model.MovieCredits
import com.saiful.person.model.PersonDetails
import com.saiful.person.model.PersonImage
import com.saiful.person.model.TvShowsCredits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class PersonDetailsVMTest : BaseViewModelTest() {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: PersonDetailsRepo = mock()
    private lateinit var viewModel: PersonDetailsVM
    private lateinit var personDetails: PersonDetails
    private lateinit var personImage: PersonImage
    private lateinit var movieCredits: MovieCredits
    private lateinit var tvShowsCredits: TvShowsCredits
    private val personId: Int = 1

    override fun setup() {
        personDetails = PersonDetails(
            id = 1,
            gender = 1,
            name = "Tom Cruse"
        )

        personImage = PersonImage(
            id = 1,
            profiles = listOf()
        )

        movieCredits = MovieCredits(
            id = 1,
            cast = listOf(),
            crew = listOf()
        )

        tvShowsCredits = TvShowsCredits(
            id = 1,
            cast = listOf(),
            crew = listOf()
        )
    }

    override fun tearDown() {
        reset(repository)
    }

    private fun initViewModel() {
        viewModel = PersonDetailsVM(
            repository
        )
    }
    @Test
    fun `verify person details api calls fetch is successful`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(
                repository.personTvShowsCredits(personId)
            ).thenReturn(
                BaseResponse.Success(tvShowsCredits)
            )
            whenever(
                repository.personMovieCredits(personId)
            ).thenReturn(
                BaseResponse.Success(movieCredits)
            )
            whenever(
                repository.personImages(personId)
            ).thenReturn(
                BaseResponse.Success(personImage)
            )
            whenever(
                repository.personDetails(personId)
            ).thenReturn(
                BaseResponse.Success(personDetails)
            )

            initViewModel()
            viewModel.fetchPersonDetails(personId)

            verify(repository, times(1)).personTvShowsCredits(any())
            verify(repository, times(1)).personMovieCredits(any())
            verify(repository, times(1)).personImages(any())
            verify(repository, times(1)).personDetails(any())

            assert(viewModel.personDetails.value!!.id == personDetails.id)
            assert(viewModel.personImageList.value!!.id == personImage.id)
            assert(viewModel.personMovieList.value!!.id == movieCredits.id)
            assert(viewModel.personShowsList.value!!.id == tvShowsCredits.id)
        }
    }
}