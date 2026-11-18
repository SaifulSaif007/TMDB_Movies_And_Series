package com.saiful.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.saiful.shared.data.api.MultiSearchApiService
import com.saiful.shared.data.repository.paging.MultiSearchPagingSource
import com.saiful.shared.model.search.MultiSearchResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MultiSearchRepository @Inject constructor(
    private val multiSearchApiService: MultiSearchApiService
) {
    fun searchMulti(query: String): Flow<PagingData<MultiSearchResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MultiSearchPagingSource(multiSearchApiService, query) }
        ).flow
    }
}
