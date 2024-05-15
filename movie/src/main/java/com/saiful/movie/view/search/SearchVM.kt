package com.saiful.movie.view.search

import androidx.paging.PagingData
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.data.repository.paging.search.MovieSearchRepo
import com.saiful.shared.model.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class SearchVM @Inject constructor(
    private val searchRepo: MovieSearchRepo
) : BaseViewModel() {

    lateinit var movieList: Flow<PagingData<Movies>>

    fun searchMovie(query: String) {
        movieList = searchRepo.searchMovies(query)
    }

}