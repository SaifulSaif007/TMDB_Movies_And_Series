package com.saiful.tvshows.data.repository

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.data.api.TvShowsApiService
import com.saiful.tvshows.model.TvShowsResponse
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DashboardRepoTest : BaseRepositoryTest() {
    private val tvShowsApiService: TvShowsApiService = mock()

    private lateinit var dashboardRepo: DashboardRepo
    private lateinit var tvShowResponse: TvShowsResponse
    private val page = 1

    override fun setup() {
        dashboardRepo = DashboardRepo(
            tvShowsApiService = tvShowsApiService
        )

        tvShowResponse = TvShowsResponse(
            page = 1,
            results = listOf(
                TvShows(
                    id = 1
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
    }

    override fun tearDown() {
        reset(tvShowsApiService)
    }

    @Test
    fun `verify trending shows fetch is successful`() {
        runTest {
            whenever(
                tvShowsApiService.trendingTvShows(page)
            ).thenReturn(
                BaseResponse.Success(tvShowResponse)
            )

            assert(dashboardRepo.getTrendingShows(page) is BaseResponse.Success)
            verify(tvShowsApiService, only()).trendingTvShows(any())
        }
    }

    @Test
    fun `verify popular shows fetch is successful`(){
        runTest {
            whenever(
                tvShowsApiService.popularTvShows(page)
            ).thenReturn(
                BaseResponse.Success(tvShowResponse)
            )

            assert(dashboardRepo.getPopularShows(page) is BaseResponse.Success)
            verify(tvShowsApiService, only()).popularTvShows(any())
        }
    }

    @Test
    fun `verify top rated shows fetch is successful`(){
        runTest {
            whenever(
                tvShowsApiService.topRatedTvShows(page)
            ).thenReturn(
                BaseResponse.Success(tvShowResponse)
            )

            assert(dashboardRepo.getTopRatedShows(page) is BaseResponse.Success)
            verify(tvShowsApiService, only()).topRatedTvShows(any())
        }
    }

    @Test
    fun `verify on air shows fetch is successful`(){
        runTest {
            whenever(
                tvShowsApiService.onAirTvShows(page)
            ).thenReturn(
                BaseResponse.Success(tvShowResponse)
            )

            assert(dashboardRepo.getOnAirShows(page) is BaseResponse.Success)
            verify(tvShowsApiService, only()).onAirTvShows(any())
        }
    }
}