package com.saiful.shared.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.model.search.MultiSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MultiSearchApiService {
    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): GenericResponse<MultiSearchResponse>
}
