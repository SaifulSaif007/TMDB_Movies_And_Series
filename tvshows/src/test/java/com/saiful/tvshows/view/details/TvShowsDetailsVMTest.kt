package com.saiful.tvshows.view.details

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.data.repository.ShowDetailsRepo
import com.saiful.tvshows.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TvShowsDetailsVMTest : BaseViewModelTest() {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: ShowDetailsRepo = mock()
    private lateinit var tvShowDetails: TvShowDetails
    private lateinit var tvShowCastResponse: TvShowCastResponse
    private lateinit var tvShowsResponse: TvShowsResponse
    private lateinit var viewModel: TvShowsDetailsVM

    private val showId = 1

    override fun setup() {
        tvShowDetails = TvShowDetails()
        tvShowCastResponse = TvShowCastResponse(
            cast = listOf()
        )
        tvShowsResponse =
            TvShowsResponse(
                page = 1,
                results = listOf(TvShows(id = 1)),
                totalPages = 1,
                totalResults = 1
            )
    }

    override fun tearDown() {
        reset(repository)
    }

    private fun initViewModel() {
        viewModel = TvShowsDetailsVM(repository)
    }

    @Test
    fun `verify fetch show details is successful`() {
        runTest {
            whenever(repository.showDetails(showId)).thenReturn(
                BaseResponse.Success(tvShowDetails)
            )
            whenever(repository.showCasts(showId)).thenReturn(
                BaseResponse.Success(tvShowCastResponse)
            )
            whenever(repository.showRecommendation(showId)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )
            whenever(repository.similarShows(showId)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )

            initViewModel()

            viewModel.fetchShowDetails(showId)

            verify(repository, times(1)).showDetails(any())
            verify(repository, times(1)).showCasts(any())
            verify(repository, times(1)).showRecommendation(any())
            verify(repository, times(1)).similarShows(any())

            assert(viewModel.showDetails.value != null)
            assert(viewModel.showCasts.value != null)
            assert(viewModel.recommendation.value!!.totalPages == 1)
            assert(viewModel.similarShow.value!!.page == 1)
        }
    }
}