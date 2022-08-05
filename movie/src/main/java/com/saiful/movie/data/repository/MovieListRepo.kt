package com.saiful.movie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.base.network.model.GenericResponse
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.model.MoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieListRepo
@Inject constructor(private val apiService: MovieApiService) {

    fun getMoviePager(apiCall: suspend (page:Int) -> GenericResponse<MoviesResponse>) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MoviePagingSource(apiCall) }
    ).flow

}