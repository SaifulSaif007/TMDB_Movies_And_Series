package com.saiful.tvshows.view.search

import androidx.paging.PagingData
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.data.repository.paging.search.ShowSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val repo: ShowSearchRepo
) : BaseViewModel() {

    lateinit var showsList: Flow<PagingData<TvShows>>

    fun searchShow(query: String) {
        showsList = repo.searchShows(query)
    }

}