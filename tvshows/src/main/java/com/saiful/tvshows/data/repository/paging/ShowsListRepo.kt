package com.saiful.tvshows.data.repository.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.base.network.model.GenericResponse
import com.saiful.tvshows.data.api.TvShowsApiService
import com.saiful.tvshows.model.TvShowsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowsListRepo
@Inject constructor() {

    fun getShowsPager(apiCall: suspend (page: Int) -> GenericResponse<TvShowsResponse>) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ShowsPagingSource(apiCall) }
    ).flow
}