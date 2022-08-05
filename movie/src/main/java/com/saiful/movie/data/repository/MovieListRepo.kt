package com.saiful.movie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.movie.data.api.MovieApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListRepo
@Inject constructor(private val apiService: MovieApiService) {

    fun getMoviePager() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MoviePagingSource(apiService) }
    ).flow

}