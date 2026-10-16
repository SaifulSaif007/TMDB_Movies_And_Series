package com.saiful.tvshows.data.repository

import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.only
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.tvshows.data.api.TvShowsApiService
import com.saiful.tvshows.model.SeasonDetails
import kotlinx.coroutines.runBlocking
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
        runBlocking {
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