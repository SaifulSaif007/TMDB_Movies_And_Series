package com.saiful.tvshows.view.season

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.tvshows.data.repository.SeasonRepo
import com.saiful.tvshows.model.SeasonDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ShowSeasonVMTest : BaseViewModelTest() {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: SeasonRepo = mock()
    private lateinit var viewModel: ShowSeasonVM
    private lateinit var seasonDetails: SeasonDetails
    private val showId = 1
    private val seasonNo = 1

    override fun setup() {
        seasonDetails = SeasonDetails(
            airDate = "2/2/2022",
            id = 1,
            name = "season 1",
            overview = "",
            posterPath = "url",
            seasonNumber = 1
        )
    }

    override fun tearDown() {
        reset(repository)
    }

    private fun initViewModel() {
        viewModel = ShowSeasonVM(repository)
    }

    @Test
    fun `verify season fetch is successful`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(
                repository.seasonDetails(showId, seasonNo)
            ).thenReturn(
                BaseResponse.Success(seasonDetails)
            )

            initViewModel()

            viewModel.fetchSeason(showId, seasonNo)

            verify(repository, times(1)).seasonDetails(any(), any())

            assert(viewModel.seasonDetails.value!!.id == seasonDetails.id)
            assert(viewModel.seasonDetails.value!!.name == seasonDetails.name)
        }
    }
}