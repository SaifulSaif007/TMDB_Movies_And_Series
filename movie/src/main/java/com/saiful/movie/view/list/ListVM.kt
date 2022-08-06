package com.saiful.movie.view.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.data.repository.MovieListRepo
import com.saiful.movie.model.MovieCategory
import com.saiful.movie.model.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(
    private val repo: MovieListRepo,
    private val apiService: MovieApiService
) : BaseViewModel() {

    lateinit var movieList: Flow<PagingData<Movies>>
    private var service = apiService::popularMovies

    fun selectedCategory(category: MovieCategory) {
        service = when (category) {
            MovieCategory.POPULAR -> {
                apiService::popularMovies
            }
            MovieCategory.NOW_PLAYING -> {
                apiService::nowPlayingMovies
            }
            MovieCategory.TOP_RATED -> {
                apiService::topRatedMovies
            }
            else -> {
                apiService::upcomingMovies
            }
        }
        movies()
    }

    fun movies() {
        movieList = repo.getMoviePager(apiCall = service).cachedIn(viewModelScope)
    }

}