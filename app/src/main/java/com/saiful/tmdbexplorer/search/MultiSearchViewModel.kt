package com.saiful.tmdbexplorer.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.shared.data.repository.MultiSearchRepository
import com.saiful.shared.model.search.MultiSearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class MultiSearchViewModel @Inject constructor(
    private val multiSearchRepository: MultiSearchRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val results: Flow<PagingData<MultiSearchResult>> = _query
        .debounce(300L)
        .flatMapLatest { q -> multiSearchRepository.searchMulti(q) }
        .cachedIn(viewModelScope)

    fun search(query: String) {
        _query.value = query
    }
}
