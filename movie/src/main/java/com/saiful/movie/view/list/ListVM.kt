package com.saiful.movie.view.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.data.repository.paging.lists.ListRepo
import com.saiful.movie.model.MovieCategory
import com.saiful.shared.model.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(
    private val repo: ListRepo,
    private val apiService: MovieApiService
) : BaseViewModel() {

    lateinit var movieList: Flow<PagingData<Movies>>
    private var service = apiService::popularMovies
    private lateinit var selectedCat: String

    fun selectedCategory(category: MovieCategory) {
        if (this::selectedCat.isInitialized) {
            return
        }
        selectedCat = category.value
        service = when (category) {

            MovieCategory.POPULAR -> apiService::popularMovies

            MovieCategory.NOW_PLAYING -> apiService::nowPlayingMovies

            MovieCategory.TOP_RATED -> apiService::topRatedMovies
            
            else -> apiService::upcomingMovies

        }
        movieList = repo.getMoviePager(service).cachedIn(viewModelScope)
    }
    
}