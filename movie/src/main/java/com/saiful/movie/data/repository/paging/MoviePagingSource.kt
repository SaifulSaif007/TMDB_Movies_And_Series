package com.saiful.movie.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.model.Movies
import com.saiful.movie.model.MoviesResponse

class MoviePagingSource(private val apiCall: suspend (page:Int)-> GenericResponse<MoviesResponse>) : PagingSource<Int, Movies>() {

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val data = apiCall.invoke(pageCount)) {
            is BaseResponse.Success -> {
                val response = data.body.results
                createPage(response, pageCount)
            }
            else -> {
                LoadResult.Error(Exception("No Movie found"))
            }
        }
    }

    private fun createPage(movies: List<Movies>, pageCount: Int) =
        LoadResult.Page(
            data = movies,
            prevKey = if (pageCount == START_PAGE_INDEX) null else pageCount - 1,
            nextKey = if (movies.isEmpty()) null else pageCount + 1
        )

    companion object {
        const val START_PAGE_INDEX = 1
    }

}