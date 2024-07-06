package com.saiful.person.view.dashboard

import com.nhaarman.mockito_kotlin.*
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
    internal var coroutineRule = MainCoroutineRule()

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

        initViewModel()
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

            viewModel.initApiCalls()

            verify(repository, times(1)).trendingPersons()
            verify(repository, times(1)).popularPersons()

            assert(viewModel.popularPersonList.value!!.totalPages == personResponse.totalPages)
            assert(viewModel.trendingPersonList.value!!.page == personResponse.page)
        }
    }

}