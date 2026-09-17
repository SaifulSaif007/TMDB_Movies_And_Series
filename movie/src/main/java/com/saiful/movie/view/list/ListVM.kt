package com.saiful.movie.view.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.data.repository.paging.lists.ListRepo
import com.saiful.shared.model.MovieCategory
import com.saiful.shared.model.Movies
import com.saiful.shared.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(
    private val repo: ListRepo,
    private val apiService: MovieApiService,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val movieList: Flow<PagingData<Movies>>

    init {
        val categoryName = savedStateHandle.toRoute<Route.MovieList>().category
        val category = MovieCategory.valueOf(categoryName)
        val service = when (category) {
            MovieCategory.POPULAR -> apiService::popularMovies
            MovieCategory.NOW_PLAYING -> apiService::nowPlayingMovies
            MovieCategory.TOP_RATED -> apiService::topRatedMovies
            MovieCategory.UPCOMING -> apiService::upcomingMovies
        }
        movieList = repo.getMoviePager(service).cachedIn(viewModelScope)
    }

    fun selectedCategory(category: MovieCategory) {
        // No longer needed as we use SavedStateHandle for initialization
    }
}