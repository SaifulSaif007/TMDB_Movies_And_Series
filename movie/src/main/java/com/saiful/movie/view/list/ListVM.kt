package com.saiful.movie.view.list

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.movie.data.repository.DashboardRepo
import com.saiful.movie.data.repository.MovieListRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListVM @Inject constructor(private val repo: MovieListRepo) : BaseViewModel(){

    val movieList = repo.getMoviePager().cachedIn(viewModelScope)

}