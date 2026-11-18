package com.saiful.shared.data.repository

import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.shared.data.api.MultiSearchApiService
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset

class MultiSearchRepositoryTest : BaseRepositoryTest() {

    private val multiSearchApiService: MultiSearchApiService = mock()
    private lateinit var repository: MultiSearchRepository

    @Before
    override fun setup() {
        repository = MultiSearchRepository(multiSearchApiService)
    }

    @After
    override fun tearDown() {
        reset(multiSearchApiService)
    }

    @Test
    fun `searchMulti returns PagingData flow`() {
        runBlocking {
            val flow = repository.searchMulti("avengers")
            assertNotNull(flow)
        }
    }
}
