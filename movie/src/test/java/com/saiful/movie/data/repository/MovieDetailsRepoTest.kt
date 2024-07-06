package com.saiful.movie.data.repository

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.model.*
import kotlinx.coroutines.test.runTest
import org.junit.*

class MovieDetailsRepoTest : BaseRepositoryTest() {

    private val movieApiService: MovieApiService = mock()
    private lateinit var movieDetailsRepo: MovieDetailsRepo
    private lateinit var movieDetailsResponse: MovieDetailsResponse
    private lateinit var movieCastResponse: MovieCastResponse
    private lateinit var movieResponse: MoviesResponse
    private val movieId = 1

    @Before
    override fun setup() {
        movieDetailsResponse = MovieDetailsResponse(
            id = movieId,
        )
        movieCastResponse = MovieCastResponse(
            id = 1,
            cast = listOf()
        )
        movieResponse = MoviesResponse(
            dates = DateRange(maximum = "10", minimum = "1"),
            page = 1,
            results = listOf(),
            totalPages = 1,
            totalResults = 10
        )
        movieDetailsRepo = MovieDetailsRepo(movieApiService)
    }

    @After
    override fun tearDown() {
        reset(movieApiService)
    }

    @Test
    fun `verify movie details fetch successful`() {
        runTest {
            whenever(movieApiService.movieDetails(movieId)).thenReturn(
                BaseResponse.Success(movieDetailsResponse)
            )

            assert(movieDetailsRepo.movieDetails(movieId) is BaseResponse.Success)

        }
    }

    @Test
    fun `verify movie details fetch returns fail result`() {
        runTest {
            whenever(movieApiService.movieDetails(movieId)).thenReturn(
                apiError
            )

            assert(movieDetailsRepo.movieDetails(movieId) is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify movie casts fetch successful`() {
        runTest {
            whenever(movieApiService.movieCast(movieId)).thenReturn(
                BaseResponse.Success(
                    movieCastResponse
                )
            )

            assert(movieDetailsRepo.movieCasts(movieId) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify movie casts fetch causes network error`() {
        runTest {
            whenever(movieApiService.movieCast(movieId)).thenReturn(
                networkError
            )

            assert(movieDetailsRepo.movieCasts(movieId) is BaseResponse.NetworkError)
        }
    }

    @Test
    fun `verify recommendation movie fetch successful`() {
        runTest {
            whenever(movieApiService.recommendation(movieId)).thenReturn(
                BaseResponse.Success(movieResponse)
            )

            assert(movieDetailsRepo.recommendation(movieId) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify recommendation movie fetch returns unknown error`() {
        runTest {
            whenever(movieApiService.recommendation(movieId)).thenReturn(
                unknownError
            )

            assert(movieDetailsRepo.recommendation(movieId) is BaseResponse.UnknownError)
        }
    }

    @Test
    fun `verify similar movie fetch successful`() {
        runTest {
            whenever(
                movieApiService.similarMovie(
                    movieId
                )
            ).thenReturn(
                BaseResponse.Success(movieResponse)
            )

            assert(movieDetailsRepo.similarMovie(movieId) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify similar movie returns api error`() {
        runTest {
            whenever(
                movieApiService.similarMovie(
                    movieId
                )
            ).thenReturn(
                apiError
            )

            assert(movieDetailsRepo.similarMovie(movieId) is BaseResponse.ApiError)
        }
    }
}