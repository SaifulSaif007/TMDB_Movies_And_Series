package com.saiful.movie.view.collection

import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.viewmodel.BaseOpsViewModel
import com.saiful.movie.data.repository.CollectionRepo
import com.saiful.movie.model.MovieCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CollectionVM
@Inject constructor(private val repo: CollectionRepo) : BaseOpsViewModel() {

    val collections = MutableStateFlow<MovieCollection?>(null)

    fun fetchCollections(collectionId: Int) {
        executeRestCodeBlock(movieCollection) {
            repo.movieCollection(collectionId)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        if (operationTag == movieCollection) {
            when (val response = data as GenericResponse<*>) {
                is BaseResponse.Success -> {
                    collections.value = response.body as MovieCollection
                }
                else -> {}
            }
        }
    }

    private companion object {
        const val movieCollection = "MOVIE_COLLECTION"
    }
}