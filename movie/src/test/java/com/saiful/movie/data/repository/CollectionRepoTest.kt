package com.saiful.movie.data.repository

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericError
import com.saiful.movie.data.api.MovieApiService
import com.saiful.movie.model.MovieCollection
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class CollectionRepoTest {
    private val apiService: MovieApiService = mock()
    private lateinit var collectionRepo: CollectionRepo
    private lateinit var movieCollection: MovieCollection
    private val id: Int = 1

    @Before
    fun setup() {
        collectionRepo = CollectionRepo(apiService)

        movieCollection =  MovieCollection(
            id = 1,
            name = "avengers",
            overview = "overview",
            posterPath = null,
            backdropPath = null,
            parts = listOf()
        )
    }

    @After
    fun tearDown() {
        reset(apiService)
    }

    @Test
    fun `verify movie collection returns success result`() {
        runBlocking {
            whenever(apiService.collections(id)).thenReturn(
                BaseResponse.Success(movieCollection)
            )

            assert(collectionRepo.movieCollection(id) is BaseResponse.Success)
        }
    }

    @Test
    fun `verify movie collection returns api error`() {
        runBlocking {
            whenever(apiService.collections(id)).thenReturn(
                BaseResponse.ApiError(
                    errorBody = GenericError(
                        status_code = "200",
                        status_message = "exception"
                    ),
                    code = 200
                )
            )

            assert(collectionRepo.movieCollection(id) is BaseResponse.ApiError)
        }
    }
}