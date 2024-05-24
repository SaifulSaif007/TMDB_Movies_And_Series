package com.saiful.tvshows.view.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.data.api.TvShowsApiService
import com.saiful.tvshows.data.repository.paging.list.ShowsListRepo
import com.saiful.tvshows.model.TvShowsCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(
    private val repo: ShowsListRepo,
    private val apiService: TvShowsApiService
) : BaseViewModel() {

    lateinit var showsList: Flow<PagingData<TvShows>>
    private var service = apiService::popularTvShows
    private lateinit var selectedCat: String

    fun selectedCategory(category: TvShowsCategory) {
        if (this::selectedCat.isInitialized) {
            return
        }
        selectedCat = category.value
        service = when (category) {
            TvShowsCategory.POPULAR -> apiService::popularTvShows
            TvShowsCategory.TOP_RATED -> apiService::topRatedTvShows
            TvShowsCategory.ON_AIR -> apiService::onAirTvShows
            else -> apiService::trendingTvShows
        }
        tvShows()
    }

    private fun tvShows() {
        showsList = repo.getShowsPager(apiCall = service).cachedIn(viewModelScope)
    }
}