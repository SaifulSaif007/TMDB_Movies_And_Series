package com.saiful.tvshows.view.dashboard

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.data.repository.DashboardRepo
import com.saiful.tvshows.model.TvShowsResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShowsDashboardVMTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

    private val dashboardRepo: DashboardRepo = mock()

    private lateinit var tvShowsResponse: TvShowsResponse
    private lateinit var viewModel: ShowsDashboardVM
    private val pageNo = 1

    override fun setup() {
        tvShowsResponse = TvShowsResponse(
            page = 1,
            results = listOf(
                TvShows(id = 1),
                TvShows(id = 2)
            ),
            totalPages = 1,
            totalResults = 2
        )
    }

    override fun tearDown() {
        reset(dashboardRepo)
    }

    private fun initViewModel() {
        viewModel = ShowsDashboardVM(dashboardRepo)
    }

    @Test
    fun `verify all dashboard tv shows fetch successful`() {
        runTest {
            whenever(dashboardRepo.getTrendingShows(pageNo)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )
            whenever(dashboardRepo.getPopularShows(pageNo)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )
            whenever(dashboardRepo.getTopRatedShows(pageNo)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )
            whenever(dashboardRepo.getOnAirShows(pageNo)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )

            initViewModel()

            verify(dashboardRepo, times(1)).getOnAirShows(any())
            verify(dashboardRepo, times(1)).getPopularShows(any())
            verify(dashboardRepo, times(1)).getTopRatedShows(any())
            verify(dashboardRepo, times(1)).getTrendingShows(any())

            assert(viewModel.onAirShowsList.value!!.totalPages == 1)
            assert(viewModel.popularShowsList.value!!.totalResults == 2)
            assert(viewModel.trendingShowsList.value!!.results.isNotEmpty())
            assert(viewModel.topRatedShowsList.value!!.totalPages == 1)
            assert(viewModel.sliderList.isNotEmpty())
            assert(viewModel.sliderLoaded.value == null)
        }
    }
}