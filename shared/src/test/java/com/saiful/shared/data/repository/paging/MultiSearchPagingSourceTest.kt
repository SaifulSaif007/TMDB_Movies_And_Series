package com.saiful.shared.data.repository.paging

import androidx.paging.PagingSource
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.shared.data.api.MultiSearchApiService
import com.saiful.shared.model.search.MultiSearchResponse
import com.saiful.shared.model.search.MultiSearchResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

class MultiSearchPagingSourceTest {

    private val apiService: MultiSearchApiService = mock()

    private val movieResult = MultiSearchResult(
        id = 1,
        mediaType = "movie",
        title = "Inception",
        voteAverage = 8.8
    )
    private val tvResult = MultiSearchResult(
        id = 2,
        mediaType = "tv",
        name = "Breaking Bad",
        voteAverage = 9.5
    )

    private val apiError = BaseResponse.ApiError(
        errorBody = GenericError(status_code = "404", status_message = "Not found"),
        code = 404
    )

    @Test
    fun `load with blank query returns empty page without calling api`() = runTest {
        val pagingSource = MultiSearchPagingSource(apiService, query = "   ")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, MultiSearchResult>
        assertTrue(page.data.isEmpty())
        assertNull(page.prevKey)
        assertNull(page.nextKey)
    }

    @Test
    fun `load success returns page with multi search results`() = runTest {
        whenever(apiService.searchMulti("avengers", 1)).thenReturn(
            BaseResponse.Success(
                MultiSearchResponse(
                    page = 1,
                    results = listOf(movieResult, tvResult),
                    totalPages = 3,
                    totalResults = 60
                )
            )
        )

        val pagingSource = MultiSearchPagingSource(apiService, query = "avengers")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, MultiSearchResult>
        assertEquals(listOf(movieResult, tvResult), page.data)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load success on last page returns nextKey as null`() = runTest {
        whenever(apiService.searchMulti("avengers", 2)).thenReturn(
            BaseResponse.Success(
                MultiSearchResponse(
                    page = 2,
                    results = listOf(movieResult),
                    totalPages = 2,
                    totalResults = 40
                )
            )
        )

        val pagingSource = MultiSearchPagingSource(apiService, query = "avengers")
        val result = pagingSource.load(
            PagingSource.LoadParams.Append(key = 2, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, MultiSearchResult>
        assertEquals(1, page.prevKey)
        assertNull(page.nextKey)
    }

    @Test
    fun `load returns error on api error`() = runTest {
        whenever(apiService.searchMulti("avengers", 1)).thenReturn(apiError)

        val pagingSource = MultiSearchPagingSource(apiService, query = "avengers")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals("Not found", error.throwable.message)
    }

    @Test
    fun `load returns error on network error`() = runTest {
        val networkException = IOException("Network down")
        whenever(apiService.searchMulti("avengers", 1)).thenReturn(
            BaseResponse.NetworkError(networkException)
        )

        val pagingSource = MultiSearchPagingSource(apiService, query = "avengers")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals(networkException, error.throwable)
    }

    @Test
    fun `load returns error on unknown error`() = runTest {
        val unknownException = RuntimeException("Something went wrong")
        whenever(apiService.searchMulti("avengers", 1)).thenReturn(
            BaseResponse.UnknownError(unknownException)
        )

        val pagingSource = MultiSearchPagingSource(apiService, query = "avengers")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals(unknownException, error.throwable)
    }
}
