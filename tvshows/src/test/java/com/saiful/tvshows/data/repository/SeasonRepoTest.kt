package com.saiful.tvshows.data.repository

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.tvshows.data.api.TvShowsApiService
import com.saiful.tvshows.model.SeasonDetails
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class SeasonRepoTest : BaseRepositoryTest() {

    private val apiService: TvShowsApiService = mock()
    private lateinit var seasonRepo: SeasonRepo
    private lateinit var seasonDetails: SeasonDetails

    override fun setup() {
        seasonRepo = SeasonRepo(apiService)

        seasonDetails = SeasonDetails(
            id = 1,
            airDate = "",
            name = "season 1",
            overview = "first season",
            posterPath = "",
            seasonNumber = 1
        )
    }

    override fun tearDown() {
        reset(apiService)
    }

    @Test
    fun `verify season details fetch is successful`() {
        runTest {
            whenever(
                apiService.seasonDetails(any(), any())
            ).thenReturn(
                BaseResponse.Success(seasonDetails)
            )

            assert(seasonRepo.seasonDetails(any(), any()) is BaseResponse.Success)
            verify(apiService, only()).seasonDetails(any(), any())
        }
    }
}