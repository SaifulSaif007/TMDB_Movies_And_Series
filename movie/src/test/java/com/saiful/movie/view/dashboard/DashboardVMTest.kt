package com.saiful.movie.view.dashboard

import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.movie.data.repository.DashboardRepo
import com.saiful.movie.model.DateRange
import com.saiful.movie.model.MoviesResponse
import com.saiful.shared.model.Movies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class DashboardVMTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

    private val repository: DashboardRepo = mock()
    private lateinit var moviesResponse: MoviesResponse

    private lateinit var viewModel: DashboardVM
    private val pageNo = 1

    override fun setup() {
        moviesResponse = MoviesResponse(
            dates = DateRange(maximum = "", minimum = ""),
            page = 1,
            results = listOf(
                Movies(id = 1),
                Movies(id = 2)
            ),
            totalPages = 2,
            totalResults = 2
        )

    }

    override fun tearDown() {
        reset(repository)
    }

    private fun initViewModel() {
        viewModel = DashboardVM(repository)
    }


    @Test
    fun `verify all dashboard movie fetch successful`() {
        runTest(coroutineRule.testDispatcher) {
            whenever(repository.getPopularMovies(pageNo)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.getNowPlayingMovies(pageNo)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.getTopRatedMovies(pageNo)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.getUpcomingMovies(pageNo)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )


            initViewModel()

            verify(repository, times(1)).getPopularMovies(any())
            verify(repository, times(1)).getNowPlayingMovies(any())
            verify(repository, times(1)).getTopRatedMovies(any())
            verify(repository, times(1)).getUpcomingMovies(any())

            assert(viewModel.uiState.value.popularMovies.isNotEmpty())
            assert(viewModel.uiState.value.nowPlayingMovies.isNotEmpty())
            assert(viewModel.uiState.value.topRatedMovies.isNotEmpty())
            assert(viewModel.uiState.value.upcomingMovies.isNotEmpty())
            assert(viewModel.uiState.value.sliderMovies.isNotEmpty())
        }
    }

}