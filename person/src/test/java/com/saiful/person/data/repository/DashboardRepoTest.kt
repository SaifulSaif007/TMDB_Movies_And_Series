package com.saiful.person.data.repository

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.model.Person
import com.saiful.person.model.PersonResponse
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class DashboardRepoTest : BaseRepositoryTest() {

    private val apiService: PersonApiService = mock()
    private lateinit var dashboardRepo: DashboardRepo
    private lateinit var personResponse: PersonResponse

    override fun setup() {
        personResponse = PersonResponse(
            page = 1,
            results = listOf(
                Person(
                    id = 1,
                    name = "Tom Cruise",
                    popularity = 8.9,
                    profilePath = "url"
                ),
                Person(
                    id = 2,
                    name = "Dwayne Johnson",
                    popularity = 8.8,
                    profilePath = "url"
                )
            ),
            totalPages = 1,
            totalResults = 2
        )

        dashboardRepo = DashboardRepo(apiService)
    }

    override fun tearDown() {
        reset(apiService)
    }


    @Test
    fun `verify popular person returns success result`() {
        runTest {
            whenever(
                apiService.popularPersons()
            ).thenReturn(
                BaseResponse.Success(personResponse)
            )

            assert(dashboardRepo.popularPersons() is BaseResponse.Success)
            verify(apiService, only()).popularPersons()
        }
    }

    @Test
    fun `verify trending person returns success result`() {
        runTest {
            whenever(
                apiService.trendingPersons()
            ).thenReturn(
                BaseResponse.Success(personResponse)
            )

            assert(dashboardRepo.trendingPersons() is BaseResponse.Success)
            verify(apiService, only()).trendingPersons()
        }
    }

}