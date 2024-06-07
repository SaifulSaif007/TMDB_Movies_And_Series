package com.saiful.shared.view

import com.saiful.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class SharedSearchVM @Inject constructor() : BaseViewModel() {

    var searchedQuery = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1
    )

    fun updateSearchQuery(query: String) {
        if (query.isNotBlank() && !query.equals(searchedQuery)){
            searchedQuery.tryEmit(query)
        }
    }

}