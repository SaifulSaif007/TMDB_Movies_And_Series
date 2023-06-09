package com.saiful.person.view.dashboard

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.person.data.repository.DashboardRepo
import com.saiful.person.model.PersonResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class PersonDashboardVMTest : BaseViewModelTest() {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: DashboardRepo = mock()
    private lateinit var viewModel: PersonDashboardVM
    private lateinit var personResponse: PersonResponse

    override fun setup() {
        personResponse = PersonResponse(
            page = 1,
            results = listOf(),
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
    fun `verify popular person fetch is successful`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(
                repository.popularPersons()
            ).thenReturn(
                BaseResponse.Success(personResponse)
            )

            initViewModel()

            verify(repository, times(1)).popularPersons()
            assert(viewModel.popularPersonList.value != null)
        }
    }

    @Test
    fun `verify trending person fetch is successful`() {
        runTest(mainCoroutineRule.testDispatcher) {
            whenever(
                repository.trendingPersons()
            ).thenReturn(
                BaseResponse.Success(personResponse)
            )

            initViewModel()

            verify(repository, times(1)).trendingPersons()
            assert(viewModel.trendingPersonList.value!!.page == personResponse.page)
        }
    }

}