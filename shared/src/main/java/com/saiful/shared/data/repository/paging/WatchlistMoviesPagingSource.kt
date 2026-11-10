package com.saiful.shared.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.shared.data.repository.AccountRepository
import com.saiful.shared.model.Movies
import com.saiful.shared.model.account.toMovie

class WatchlistMoviesPagingSource(
    private val accountRepository: AccountRepository,
    private val accountId: Int
) : PagingSource<Int, Movies>() {

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val data = accountRepository.getWatchlistMovies(accountId, pageCount)) {
            is BaseResponse.Success -> {
                val results = data.body.results.map { it.toMovie() }
                val totalPages = data.body.totalPages
                LoadResult.Page(
                    data = results,
                    prevKey = if (pageCount == START_PAGE_INDEX) null else pageCount - 1,
                    nextKey = if (results.isEmpty() || pageCount >= totalPages) null else pageCount + 1
                )
            }
            is BaseResponse.ApiError -> LoadResult.Error(Exception(data.errorBody.status_message))
            is BaseResponse.NetworkError -> LoadResult.Error(data.error)
            is BaseResponse.UnknownError -> LoadResult.Error(data.error ?: Exception("Unknown error"))
        }
    }

    companion object {
        const val START_PAGE_INDEX = 1
    }
}
