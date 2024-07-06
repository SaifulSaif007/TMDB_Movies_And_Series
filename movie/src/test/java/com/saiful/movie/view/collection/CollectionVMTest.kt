package com.saiful.movie.view.collection

import com.nhaarman.mockito_kotlin.*
import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseViewModelTest
import com.saiful.base_unit_test.rules.MainCoroutineRule
import com.saiful.movie.data.repository.CollectionRepo
import com.saiful.movie.model.MovieCollection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionVMTest : BaseViewModelTest() {
    @get:Rule
    internal var mainCoroutineRule = MainCoroutineRule()

    private val collectionRepo: CollectionRepo = mock()
    private lateinit var collectionVM: CollectionVM
    private lateinit var movieCollection: MovieCollection
    private val id = 1


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
        runTest {
            whenever(collectionRepo.movieCollection(id)).thenReturn(
                BaseResponse.Success(
                    movieCollection
                )
            )

            initViewModel()
            collectionVM.fetchCollections(id)

            verify(collectionRepo, only()).movieCollection(any())
            assert(collectionVM.collections.toString().isNotEmpty())
        }
    }


}