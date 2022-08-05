package com.saiful.movie.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.model.Movies
import com.saiful.movie.model.PopularMovies

class MoviePagingSource(private val apiService: MovieApiService) : PagingSource<Int, Movies>() {

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val data = apiService.popularMovies(pageCount)) {
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