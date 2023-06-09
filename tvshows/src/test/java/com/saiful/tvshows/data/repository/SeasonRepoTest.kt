package com.saiful.tvshows.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.only
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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