package com.saiful.tvshows.data.repository.paging.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.model.TvShowsResponse

class ListPagingSource(private val apiCall: suspend (page: Int) -> GenericResponse<TvShowsResponse>) :
    PagingSource<Int, TvShows>() {
    override fun getRefreshKey(state: PagingState<Int, TvShows>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShows> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val data = apiCall.invoke(pageCount)) {
            is BaseResponse.Success -> {
                val response = data.body.results
                createPage(response, pageCount)
            }
            else -> {
                LoadResult.Error(Exception("No Tv Shows found"))
            }
        }
    }

    private fun createPage(shows: List<TvShows>, pageCount: Int) =
        LoadResult.Page(
            data = shows,
            prevKey = if (pageCount == START_PAGE_INDEX) null else pageCount - 1,
            nextKey = if (shows.isEmpty()) null else pageCount + 1
        )

    companion object {
        const val START_PAGE_INDEX = 1
    }
}