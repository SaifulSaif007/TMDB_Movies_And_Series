package com.saiful.tvshows.data.repository.paging.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.data.api.TvShowsApiService

class ShowSearchPagingSource(private val apiService: TvShowsApiService, private val query: String) :
    PagingSource<Int, TvShows>() {
    override fun getRefreshKey(state: PagingState<Int, TvShows>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShows> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val response = apiService.searchShow(query = query, page = pageCount)) {
            is BaseResponse.Success -> {
                val responseBody = response.body.results
                createPage(responseBody, pageCount)
            }

            else -> LoadResult.Error(Exception("No tv shows found"))
        }
    }


    private fun createPage(shows: List<TvShows>, page: Int) =
        LoadResult.Page(
            data = shows,
            prevKey = if (page == START_PAGE_INDEX) null else page - 1,
            nextKey = if (shows.isEmpty()) null else page + 1
        )

    private companion object {
        const val START_PAGE_INDEX = 1
    }
}