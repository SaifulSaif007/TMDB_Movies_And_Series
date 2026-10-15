package com.saiful.person.view.dashboard

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.person.data.repository.DashboardRepo
import com.saiful.person.model.PersonResponse
import com.saiful.shared.model.Person
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PersonDashboardVMTest : BaseViewModelTest() {

    @get:Rule
    internal val mainCoroutineRule = MainCoroutineRule()

    private val repository: DashboardRepo = mock()
    private lateinit var viewModel: PersonDashboardVM
    private lateinit var personResponse: PersonResponse
    override fun setup() {
        personResponse = PersonResponse(
            page = 1,
            results = listOf(
                Person(
                    id = 1,
                    name = "Tom Cruise",
                    popularity = 8.9,
                    profilePath = "url"
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
    }

    override fun tearDown() {
        reset(repository)
    }

    private fun initViewModel() {
        viewModel = PersonDashboardVM(repository)
    }

    @Test
    fun `verify trending person & popular person fetch is successful`() {
        runTest {
            whenever(
                repository.popularPersons()
            ).thenReturn(
                BaseResponse.Success(personResponse)
            )
            whenever(
                repository.trendingPersons()
            ).thenReturn(
                BaseResponse.Success(personResponse)
            )

            initViewModel()

            // Advance the test scheduler to ensure all coroutines complete
            testScheduler.advanceUntilIdle()

            verify(repository, times(1)).trendingPersons()
            verify(repository, times(1)).popularPersons()

            assert(viewModel.uiState.value.popularPersons.isNotEmpty())
            assert(viewModel.uiState.value.trendingPersons.isNotEmpty())
        }
    }

}