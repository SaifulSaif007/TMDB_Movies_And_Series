package com.saiful.movie.data.repository

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.model.DateRange
import com.saiful.movie.model.MovieCastResponse
import com.saiful.movie.model.MovieDetailsResponse
import com.saiful.movie.model.MoviesResponse
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

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
        runBlocking {
            whenever(movieApiService.movieDetails(movieId)).thenReturn(
                BaseResponse.Success(movieDetailsResponse)
            )

            assert(movieDetailsRepo.movieDetails(movieId) is BaseResponse.Success)

        }
    }

    @Test
    fun `verify movie details fetch returns fail result`() {
        runBlocking {
            whenever(movieApiService.movieDetails(movieId)).thenReturn(
                apiError
            )

            assert(movieDetailsRepo.movieDetails(movieId) is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify movie casts fetch successful`() {
        runBlocking {
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
        runBlocking {
            whenever(movieApiService.movieCast(movieId)).thenReturn(
                networkError
            )

            assert(movieDetailsRepo.movieCasts(movieId) is BaseResponse.NetworkError)
        }
    }

    @Test
    fun `verify recommendation movie fetch successful`() {
        runBlocking {
            whenever(movieApiService.recommendation(movieId)).thenReturn(
                BaseResponse.Success(movieResponse)
            )

            assert(movieDetailsRepo.recommendation(movieId) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify recommendation movie fetch returns unknown error`() {
        runBlocking {
            whenever(movieApiService.recommendation(movieId)).thenReturn(
                unknownError
            )

            assert(movieDetailsRepo.recommendation(movieId) is BaseResponse.UnknownError)
        }
    }

    @Test
    fun `verify similar movie fetch successful`() {
        runBlocking {
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
        runBlocking {
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