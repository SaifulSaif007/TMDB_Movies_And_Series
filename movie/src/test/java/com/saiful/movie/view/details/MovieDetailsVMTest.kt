package com.saiful.movie.view.details

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.movie.data.repository.MovieDetailsRepo
import com.saiful.movie.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MovieDetailsVMTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

    private val repository: MovieDetailsRepo = mock()

    private lateinit var movieDetailsResponse: MovieDetailsResponse
    private lateinit var movieCastResponse: MovieCastResponse
    private lateinit var moviesResponse: MoviesResponse

    private lateinit var viewModel: MovieDetailsVM

    override fun setup() {
        movieDetailsResponse = MovieDetailsResponse(
            adult = false
        )
        movieCastResponse = MovieCastResponse(
            id = 1,
            cast = emptyList()
        )
        moviesResponse = MoviesResponse(
            dates = DateRange(maximum = "", minimum = ""),
            page = 1,
            results = emptyList(),
            totalPages = 2,
            totalResults = 2
        )
    }

    override fun tearDown() {
        reset(repository)
    }

    private fun initViewModel() {
        viewModel = MovieDetailsVM(
            repo = repository
        )
    }

    @Test
    fun `verify movie details data fetch successful`() {
        runTest {
            whenever(repository.movieDetails(1)).thenReturn(
                BaseResponse.Success(movieDetailsResponse)
            )
            whenever(repository.movieCasts(1)).thenReturn(
                BaseResponse.Success(movieCastResponse)
            )
            whenever(repository.similarMovie(1)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )
            whenever(repository.recommendation(1)).thenReturn(
                BaseResponse.Success(moviesResponse)
            )


            initViewModel()

            viewModel.fetchMovieDetails(1)

            verify(repository, times(1)).movieDetails(1)
            verify(repository, times(1)).movieCasts(1)
            verify(repository, times(1)).similarMovie(1)
            verify(repository, times(1)).recommendation(1)

            assert(viewModel.similar.value!!.page == 1)
            assert(viewModel.recommendation.value!!.totalPages == 2)
            assert(viewModel.movieDetailsResponse.value!!.adult == false)
            assert(viewModel.movieCast.value!!.id == 1)
        }
    }

}