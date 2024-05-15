package com.saiful.movie.data.repository.paging.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.movie.data.api.MovieApiService
import com.saiful.shared.model.Movies

class MovieSearchPagingSource(private val api: MovieApiService, private val query: String) :
    PagingSource<Int, Movies>() {
    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val response = api.searchMovies(query = query, page = pageCount)) {
            is BaseResponse.Success -> {
                val responseBody = response.body.results
                createPage(responseBody, pageCount)
            }

            else -> LoadResult.Error(Exception("No movie found"))
        }
    }


    private fun createPage(movies: List<Movies>, page: Int) =
        LoadResult.Page(
            data = movies,
            prevKey = if (page == START_PAGE_INDEX) null else page - 1,
            nextKey = if (movies.isEmpty()) null else page + 1
        )

    private companion object {
        const val START_PAGE_INDEX = 1
    }
}