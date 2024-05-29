package com.saiful.movie.data.repository.paging.lists

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.base.network.model.GenericResponse
import com.saiful.movie.model.MoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepo
@Inject constructor() {

    fun getMoviePager(apiCall: suspend (page:Int) -> GenericResponse<MoviesResponse>) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ListPagingSource(apiCall) }
    ).flow

}