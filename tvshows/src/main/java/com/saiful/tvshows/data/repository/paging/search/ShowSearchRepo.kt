package com.saiful.tvshows.data.repository.paging.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.tvshows.data.api.TvShowsApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowSearchRepo @Inject constructor(private val apiService: TvShowsApiService) {

    fun searchShows(query: String) =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
            pagingSourceFactory = { ShowSearchPagingSource(apiService, query) }
        ).flow

}