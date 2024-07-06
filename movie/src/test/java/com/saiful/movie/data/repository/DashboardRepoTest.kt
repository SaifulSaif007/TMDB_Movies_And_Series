package com.saiful.movie.data.repository

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.model.DateRange
import com.saiful.movie.model.MoviesResponse
import kotlinx.coroutines.test.runTest
import org.junit.*

class DashboardRepoTest : BaseRepositoryTest() {
    private val movieApiService: MovieApiService = mock()
    private lateinit var dashboardRepo: DashboardRepo
    private lateinit var movieResponse: MoviesResponse
    private val pageNo: Int = 1

    @Before
    override fun setup() {
        dashboardRepo = DashboardRepo(movieApiService)

        movieResponse = MoviesResponse(
            dates = DateRange(maximum = "10", minimum = "1"),
            page = 1,
            results = listOf(),
            totalPages = 1,
            totalResults = 10
        )
    }

    @After
    override fun tearDown() {
        reset(movieApiService)
    }

    @Test
    fun `verify popular movie fetch returns success result`() {
        runTest {
            whenever(movieApiService.popularMovies(pageNo)).thenReturn(
                BaseResponse.Success(movieResponse)
            )

            assert(dashboardRepo.getPopularMovies(pageNo) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify popular movie fetch causes api error`() {
        runTest {
            whenever(movieApiService.popularMovies(pageNo)).thenReturn(
                apiError
            )

            assert(dashboardRepo.getPopularMovies(1) is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify now playing movie fetch returns success result`() {
        runTest {
            whenever(movieApiService.nowPlayingMovies(pageNo)).thenReturn(
                BaseResponse.Success(movieResponse)
            )

            assert(dashboardRepo.getNowPlayingMovies(pageNo) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify now playing movie fetch returns network error`() {
        runTest {
            whenever(movieApiService.nowPlayingMovies(pageNo)).thenReturn(
               networkError
            )

            assert(dashboardRepo.getNowPlayingMovies(pageNo) is BaseResponse.NetworkError)
        }
    }

    @Test
    fun `verify top rated movie fetch returns success result`() {
        runTest {
            whenever(movieApiService.topRatedMovies(pageNo)).thenReturn(
                BaseResponse.Success(movieResponse)
            )

            assert(dashboardRepo.getTopRatedMovies(pageNo) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify top rated movie fetch returns unknown error`() {
        runTest {
            whenever(movieApiService.topRatedMovies(pageNo)).thenReturn(
                unknownError
            )

            assert(dashboardRepo.getTopRatedMovies(pageNo) is BaseResponse.UnknownError)
        }
    }

    @Test
    fun `verify upcoming movie fetch returns success result`() {
        runTest {
            whenever(movieApiService.upcomingMovies(pageNo)).thenReturn(
                BaseResponse.Success(movieResponse)
            )

            assert(dashboardRepo.getUpcomingMovies(pageNo) is BaseResponse.Success)
        }
    }
}