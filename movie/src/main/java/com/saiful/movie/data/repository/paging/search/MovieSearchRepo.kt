package com.saiful.movie.data.repository.paging.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.movie.data.api.MovieApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieSearchRepo
@Inject constructor(private val apiService: MovieApiService) {

    fun searchMovies(query: String) =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
            pagingSourceFactory = { MovieSearchPagingSource(apiService, query) }
        ).flow

}