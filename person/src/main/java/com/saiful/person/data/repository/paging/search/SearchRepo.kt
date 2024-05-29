package com.saiful.person.data.repository.paging.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.person.data.api.PersonApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepo @Inject constructor(
    private val apiService: PersonApiService
) {

    fun personSearchPager(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { SearchPagingSource(apiService, query) }
    ).flow
}