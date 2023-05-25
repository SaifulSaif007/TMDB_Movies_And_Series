package com.saiful.movie.view.dashboard

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
            whenever(repository.getPopularMovies(any())).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.getNowPlayingMovies(any())).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.getTopRatedMovies(any())).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.getUpcomingMovies(any())).thenReturn(
                BaseResponse.Success(moviesResponse)
            )


            initViewModel()

            verify(repository, times(1)).getPopularMovies(any())
            verify(repository, times(1)).getNowPlayingMovies(any())
            verify(repository, times(1)).getTopRatedMovies(any())
            verify(repository, times(1)).getUpcomingMovies(any())

            assert(viewModel.popularMoviesList.value!!.totalPages == 2)
            assert(viewModel.nowPlayingMoviesList.value!!.results.isNotEmpty())
            assert(viewModel.topRatedMoviesList.value!!.page == 1)
            assert(viewModel.upcomingMoviesList.value!!.totalResults == 2)
            assert(viewModel.sliderList.isNotEmpty())
            assert(viewModel.sliderLoaded.value == null)
        }
    }

}