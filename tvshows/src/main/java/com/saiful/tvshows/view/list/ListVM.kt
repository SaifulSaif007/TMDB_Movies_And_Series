package com.saiful.tvshows.view.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.model.TvShows
import com.saiful.tvshows.data.api.TvShowsApiService
import com.saiful.tvshows.data.repository.paging.list.ListRepo
import com.saiful.shared.model.TvShowsCategory
import com.saiful.shared.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(
    private val repo: ListRepo,
    private val apiService: TvShowsApiService,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val showsList: Flow<PagingData<TvShows>>

    init {
        val categoryName = savedStateHandle.toRoute<Route.TvShowsList>().category
        val category = TvShowsCategory.valueOf(categoryName)
        val service = when (category) {
            TvShowsCategory.POPULAR -> apiService::popularTvShows
            TvShowsCategory.TOP_RATED -> apiService::topRatedTvShows
            TvShowsCategory.ON_AIR -> apiService::onAirTvShows
            TvShowsCategory.TRENDING -> apiService::trendingTvShows
        }
        showsList = repo.getShowsPager(apiCall = service).cachedIn(viewModelScope)
    }

    fun selectedCategory(category: TvShowsCategory) {
        // No longer needed
    }
}