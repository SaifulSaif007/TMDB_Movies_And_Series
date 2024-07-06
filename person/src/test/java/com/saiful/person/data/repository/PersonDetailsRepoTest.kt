package com.saiful.person.data.repository

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.model.*
import com.saiful.shared.model.Image
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class PersonDetailsRepoTest : BaseRepositoryTest() {

    private val apiService: PersonApiService = mock()
    private lateinit var repository: PersonDetailsRepo

    private lateinit var personDetails: PersonDetails
    private lateinit var personImage: PersonImage
    private lateinit var movieCredits: MovieCredits
    private lateinit var tvShowsCredits: TvShowsCredits

    override fun setup() {
        repository = PersonDetailsRepo(
            apiService = apiService
        )

        personDetails = PersonDetails(
            id = 1,
            gender = 1,
            name = "Tom Cruse"
        )

        personImage = PersonImage(
            id = 1,
            profiles = listOf(Image(), Image())
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
        reset(apiService)
    }

    @Test
    fun `verify person details fetch is successful`() {
        runTest {
            whenever(
                apiService.personDetails(any())
            ).thenReturn(
                BaseResponse.Success(personDetails)
            )

            assert(repository.personDetails(any()) is BaseResponse.Success)
            verify(apiService, only()).personDetails(any())

        }
    }


    @Test
    fun `verify person image fetch is successful`() {
        runTest {
            whenever(
                apiService.personImage(any())
            ).thenReturn(
                BaseResponse.Success(personImage)
            )

            assert(repository.personImages(any()) is BaseResponse.Success)
            verify(apiService, only()).personImage(any())

        }
    }

    @Test
    fun `verify person movie credits fetch is successful`() {
        runTest {
            whenever(
                apiService.movieCredits(any())
            ).thenReturn(
                BaseResponse.Success(movieCredits)
            )

            assert(repository.personMovieCredits(any()) is BaseResponse.Success)
            verify(apiService, only()).movieCredits(any())

        }
    }

    @Test
    fun `verify person shows credits fetch is successful`(){
        runTest {
            whenever(
                apiService.tvShowsCredits(any())
            ).thenReturn(
                BaseResponse.Success(tvShowsCredits)
            )

            assert(repository.personTvShowsCredits(1) is BaseResponse.Success)
            verify(apiService, only()).tvShowsCredits(any())

        }
    }

}