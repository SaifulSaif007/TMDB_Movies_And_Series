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
import com.saiful.tvshows.model.TvShowCastResponse
import com.saiful.tvshows.model.TvShowDetails
import com.saiful.tvshows.model.TvShowsResponse
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class ShowDetailsRepoTest : BaseRepositoryTest() {

    private val apiService: TvShowsApiService = mock()
    private lateinit var showDetailsRepo: ShowDetailsRepo
    private lateinit var tvShowDetails: TvShowDetails
    private lateinit var tvShowCastResponse: TvShowCastResponse
    private lateinit var tvShowsResponse: TvShowsResponse

    private val showId: Int = 1

    override fun setup() {
        showDetailsRepo = ShowDetailsRepo(apiService)

        tvShowDetails = TvShowDetails(
            name = "show time"
        )

        tvShowCastResponse = TvShowCastResponse(
            id = 1,
            cast = emptyList()
        )

        tvShowsResponse = TvShowsResponse(
            page = 1,
            results = listOf(),
            totalPages = 1,
            totalResults = 1
        )
    }

    override fun tearDown() {
        reset(apiService)
    }

    @Test
    fun `verify show details fetch is successful`() {
        runBlocking {
            whenever(
                apiService.showDetails(showId)
            ).thenReturn(
                BaseResponse.Success(tvShowDetails)
            )

            assert(showDetailsRepo.showDetails(showId) is BaseResponse.Success)
            verify(apiService, only()).showDetails(any(), any())
        }
    }

    @Test
    fun `verify show casts fetch is successful`() {
        runBlocking {
            whenever(
                apiService.showCasts(showId)
            ).thenReturn(
                BaseResponse.Success(tvShowCastResponse)
            )

            assert(showDetailsRepo.showCasts(showId) is BaseResponse.Success)
            verify(apiService, only()).showCasts(any())
        }
    }

    @Test
    fun `verify show recommendation fetch is successful`() {
        runBlocking {
            whenever(
                apiService.recommendations(showId)
            ).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )

            assert(showDetailsRepo.showRecommendation(showId) is BaseResponse.Success)
            verify(apiService, only()).recommendations(any())
        }
    }

    @Test
    fun `verify similar show fetch is successful`() {
        runBlocking {
            whenever(
                apiService.similarShows(showId)
            ).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )

            assert(showDetailsRepo.similarShows(showId) is BaseResponse.Success)
            verify(apiService, only()).similarShows(any())
        }
    }
}