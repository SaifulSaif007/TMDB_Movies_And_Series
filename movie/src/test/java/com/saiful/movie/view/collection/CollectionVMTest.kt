package com.saiful.movie.view.collection

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.only
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.movie.data.repository.CollectionRepo
import com.saiful.movie.model.MovieCollection
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CollectionVMTest : BaseViewModelTest() {
    private val collectionRepo: CollectionRepo = mock()
    private lateinit var collectionVM: CollectionVM
    private lateinit var movieCollection: MovieCollection


    override fun setup() {
        movieCollection =  MovieCollection(
            id = 1,
            name = "avengers",
            overview = "overview",
            posterPath = null,
            backdropPath = null,
            parts = listOf()
        )
    }

    override fun tearDown() {
        reset(collectionRepo)
    }

    private fun initViewModel(){
        collectionVM = CollectionVM(
            collectionRepo
        )
    }

    @Test
    fun `verify fetch collections returns success result`() {
        runBlocking {
            whenever(collectionRepo.movieCollection(any())).thenReturn(
                BaseResponse.Success(
                    movieCollection
                )
            )

            initViewModel()
            collectionVM.fetchCollections(1)

            verify(collectionRepo, only()).movieCollection(any())
            assert(collectionVM.collections.toString().isNotEmpty())
        }
    }


}