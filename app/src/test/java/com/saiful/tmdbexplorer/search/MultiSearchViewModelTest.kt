package com.saiful.tmdbexplorer.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.shared.data.repository.MultiSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
internal class MultiSearchViewModelTest : BaseViewModelTest() {

    @get:Rule
    internal var coroutineRule = MainCoroutineRule()

    private val multiSearchRepository: MultiSearchRepository = mock()
    private lateinit var viewModel: MultiSearchViewModel

    override fun setup() {
        whenever(multiSearchRepository.searchMulti("")).thenReturn(flowOf(PagingData.empty()))
    }

    override fun tearDown() {
        if (::viewModel.isInitialized) {
            viewModel.viewModelScope.coroutineContext.cancelChildren()
        }
        reset(multiSearchRepository)
    }

    private fun initViewModel() {
        viewModel = MultiSearchViewModel(multiSearchRepository)
    }

    @Test
    fun `initial query is empty`() = runTest {
        initViewModel()
        assertEquals("", viewModel.query.value)
    }

    @Test
    fun `search updates query value`() = runTest {
        initViewModel()
        viewModel.search("avengers")
        assertEquals("avengers", viewModel.query.value)
    }

    @Test
    fun `results flow emits for searched query`() = runTest {
        whenever(multiSearchRepository.searchMulti("avengers")).thenReturn(flowOf(PagingData.empty()))
        initViewModel()

        val job = launch {
            viewModel.results.collect {}
        }

        viewModel.search("avengers")
        advanceTimeBy(350L)
        advanceUntilIdle()

        job.cancel()
    }
}
