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
class TvShowsDetailsVMTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

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

        initViewModel()
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
            viewModel.fetchShowDetails(showId)

            verify(repository, atLeastOnce()).showDetails(any())
            assert(viewModel.showDetails.value != null)
        }
    }

    @Test
    fun `verify fetch show cast is successful`() {
        runTest {
            whenever(repository.showCasts(showId)).thenReturn(
                BaseResponse.Success(tvShowCastResponse)
            )
            viewModel.fetchShowCasts(showId)

            verify(repository, atLeastOnce()).showCasts(any())

            assert(viewModel.showCasts.value != null)

        }
    }

    @Test
    fun `verify fetch show recommendation is successful`() {
        runTest {
            whenever(repository.showRecommendation(showId)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )

            viewModel.fetchShowRecommendation(showId)

            verify(repository, atLeastOnce()).showRecommendation(any())

            assert(viewModel.recommendation.value!!.totalPages == 1)

        }
    }

    @Test
    fun `verify fetch similar show is successful`() {
        runTest {
            whenever(repository.similarShows(showId)).thenReturn(
                BaseResponse.Success(tvShowsResponse)
            )

            viewModel.fetchSimilarShow(showId)

            verify(repository, atLeastOnce()).similarShows(any())

            assert(viewModel.similarShow.value!!.page == 1)
        }
    }
}