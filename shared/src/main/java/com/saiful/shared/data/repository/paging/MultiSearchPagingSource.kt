package com.saiful.shared.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.shared.data.api.MultiSearchApiService
import com.saiful.shared.model.search.MultiSearchResult

class MultiSearchPagingSource(
    private val apiService: MultiSearchApiService,
    private val query: String
) : PagingSource<Int, MultiSearchResult>() {

    override fun getRefreshKey(state: PagingState<Int, MultiSearchResult>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MultiSearchResult> {
        if (query.isBlank()) {
            return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }

        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val data = apiService.searchMulti(query, pageCount)) {
            is BaseResponse.Success -> {
                val results = data.body.results
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
