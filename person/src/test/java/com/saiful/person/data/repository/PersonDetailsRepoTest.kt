package com.saiful.person.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.only
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.model.Image
import com.saiful.person.model.MovieCredits
import com.saiful.person.model.PersonDetails
import com.saiful.person.model.PersonImage
import com.saiful.person.model.TvShowsCredits
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class PersonDetailsRepoTest : BaseRepositoryTest() {

    private val apiService: PersonApiService = mock()
    private lateinit var repository: PersonDetailsRepo

    private lateinit var personDetails: PersonDetails
    private lateinit var personImage: PersonImage
    private lateinit var movieCredits: MovieCredits
    private lateinit var tvShowsCredits: TvShowsCredits
    private val personId: Int = 1

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
        runBlocking {
            whenever(
                apiService.personDetails(personId)
            ).thenReturn(
                BaseResponse.Success(personDetails)
            )

            assert(repository.personDetails(personId) is BaseResponse.Success)
            verify(apiService, only()).personDetails(any())

        }
    }


    @Test
    fun `verify person image fetch is successful`() {
        runBlocking {
            whenever(
                apiService.personImage(personId)
            ).thenReturn(
                BaseResponse.Success(personImage)
            )

            assert(repository.personImages(personId) is BaseResponse.Success)
            verify(apiService, only()).personImage(any())

        }
    }

    @Test
    fun `verify person movie credits fetch is successful`() {
        runBlocking {
            whenever(
                apiService.movieCredits(personId)
            ).thenReturn(
                BaseResponse.Success(movieCredits)
            )

            assert(repository.personMovieCredits(personId) is BaseResponse.Success)
            verify(apiService, only()).movieCredits(any())

        }
    }

    @Test
    fun `verify person shows credits fetch is successful`(){
        runBlocking {
            whenever(
                apiService.tvShowsCredits(personId)
            ).thenReturn(
                BaseResponse.Success(tvShowsCredits)
            )

            assert(repository.personTvShowsCredits(personId) is BaseResponse.Success)
            verify(apiService, only()).tvShowsCredits(any())

        }
    }

}