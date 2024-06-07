package com.saiful.person.view.search

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.data.repository.paging.search.SearchPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchVM @Inject constructor(
    private val apiService: PersonApiService,
) : BaseViewModel() {

    private var searchQuery = MutableStateFlow("")

    var personList = searchQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(apiService, query) }
        ).flow
    }.cachedIn(viewModelScope)

    fun searchPersons(query: String) {
        if (query.isNotBlank() && query != searchQuery.value) {
            searchQuery.value = query
        }
    }
}