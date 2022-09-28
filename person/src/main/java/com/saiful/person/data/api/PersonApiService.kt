package com.saiful.person.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.person.model.PersonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PersonApiService {

    @GET("person/popular")
    suspend fun popularPersons(
        @Query("page") page: Int
    ): GenericResponse<PersonResponse>

    @GET("trending/person/week")
    suspend fun trendingPersons(
        @Query("page") page: Int
    ): GenericResponse<PersonResponse>
}