package com.saiful.shared.data.repository.paging

import androidx.paging.PagingSource
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.account.AccountMediaItem
import com.saiful.shared.model.account.AccountMediaListResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AccountPagingSourcesTest {

    private val accountRepository: AccountRepository = mock()
    private val accountId = 12345

    private val movieItem = AccountMediaItem(id = 1, title = "Inception", voteAverage = 8.8)
    private val tvItem = AccountMediaItem(id = 2, name = "Breaking Bad", voteAverage = 9.5)

    private val apiError = BaseResponse.ApiError(
        errorBody = GenericError(status_code = "404", status_message = "Not found"),
        code = 404
    )

    @Test
    fun `FavoriteMoviesPagingSource load success returns page with mapped movies`() = runTest {
        whenever(accountRepository.getFavoriteMovies(accountId, 1)).thenReturn(
            BaseResponse.Success(
                AccountMediaListResponse(
                    page = 1,
                    results = listOf(movieItem),
                    totalPages = 2,
                    totalResults = 2
                )
            )
        )

        val pagingSource = FavoriteMoviesPagingSource(accountRepository, accountId)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Movies>
        assertEquals(1, page.data.size)
        assertEquals("Inception", page.data[0].title)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `FavoriteMoviesPagingSource load error returns LoadResult Error`() = runTest {
        whenever(accountRepository.getFavoriteMovies(accountId, 1)).thenReturn(apiError)

        val pagingSource = FavoriteMoviesPagingSource(accountRepository, accountId)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }

    @Test
    fun `FavoriteTvShowsPagingSource load success returns page with mapped shows`() = runTest {
        whenever(accountRepository.getFavoriteTvShows(accountId, 1)).thenReturn(
            BaseResponse.Success(
                AccountMediaListResponse(
                    page = 1,
                    results = listOf(tvItem),
                    totalPages = 1,
                    totalResults = 1
                )
            )
        )

        val pagingSource = FavoriteTvShowsPagingSource(accountRepository, accountId)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, TvShows>
        assertEquals(1, page.data.size)
        assertEquals("Breaking Bad", page.data[0].name)
        assertEquals(null, page.nextKey)
    }

    @Test
    fun `WatchlistMoviesPagingSource load success returns page with mapped movies`() = runTest {
        whenever(accountRepository.getWatchlistMovies(accountId, 1)).thenReturn(
            BaseResponse.Success(
                AccountMediaListResponse(
                    page = 1,
                    results = listOf(movieItem),
                    totalPages = 3,
                    totalResults = 3
                )
            )
        )

        val pagingSource = WatchlistMoviesPagingSource(accountRepository, accountId)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, Movies>
        assertEquals(1, page.data.size)
        assertEquals("Inception", page.data[0].title)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `WatchlistTvShowsPagingSource load success returns page with mapped shows`() = runTest {
        whenever(accountRepository.getWatchlistTvShows(accountId, 1)).thenReturn(
            BaseResponse.Success(
                AccountMediaListResponse(
                    page = 1,
                    results = listOf(tvItem),
                    totalPages = 1,
                    totalResults = 1
                )
            )
        )

        val pagingSource = WatchlistTvShowsPagingSource(accountRepository, accountId)
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = 1, loadSize = 20, placeholdersEnabled = false)
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page<Int, TvShows>
        assertEquals(1, page.data.size)
        assertEquals("Breaking Bad", page.data[0].name)
        assertEquals(null, page.nextKey)
    }
}
