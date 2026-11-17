package com.saiful.shared.data.repository.paging

import androidx.paging.PagingSource
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.shared.data.repository.DiscoverRepository
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.account.AccountMediaTab
import com.saiful.shared.model.discover.DiscoverFilter
import com.saiful.shared.model.discover.DiscoverMoviesResponse
import com.saiful.shared.model.discover.DiscoverTvShowsResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DiscoverPagingSourcesTest {

    private val discoverRepository: DiscoverRepository = mock()
    private val filter = DiscoverFilter(mediaTab = AccountMediaTab.MOVIES)

    private val movieItem = Movies(id = 1, title = "Inception", voteAverage = 8.8)
    private val tvItem = TvShows(id = 2, name = "Breaking Bad", voteAverage = 9.5)

    private val apiError = BaseResponse.ApiError(
        errorBody = GenericError(status_code = "404", status_message = "Not found"),
        code = 404
    )

    @Test
    fun `DiscoverMoviesPagingSource load success returns page with movies`() = runTest {
        whenever(discoverRepository.discoverMovies(1, filter)).thenReturn(
            BaseResponse.Success(
                DiscoverMoviesResponse(
                    page = 1,
                    results = listOf(movieItem),
                    totalPages = 2,
                    totalResults = 2
                )
            )
        )

        val pagingSource = DiscoverMoviesPagingSource(discoverRepository, filter)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Movies>
        assertEquals(listOf(movieItem), page.data)
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `DiscoverMoviesPagingSource load returns error on failure`() = runTest {
        whenever(discoverRepository.discoverMovies(1, filter)).thenReturn(apiError)

        val pagingSource = DiscoverMoviesPagingSource(discoverRepository, filter)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }

    @Test
    fun `DiscoverTvShowsPagingSource load success returns page with tv shows`() = runTest {
        val tvFilter = DiscoverFilter(mediaTab = AccountMediaTab.TV_SHOWS)
        whenever(discoverRepository.discoverTvShows(1, tvFilter)).thenReturn(
            BaseResponse.Success(
                DiscoverTvShowsResponse(
                    page = 1,
                    results = listOf(tvItem),
                    totalPages = 1,
                    totalResults = 1
                )
            )
        )

        val pagingSource = DiscoverTvShowsPagingSource(discoverRepository, tvFilter)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, TvShows>
        assertEquals(listOf(tvItem), page.data)
        assertEquals(null, page.prevKey)
        assertEquals(null, page.nextKey) // Last page
    }
}
