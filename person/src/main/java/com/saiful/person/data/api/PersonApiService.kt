package com.saiful.person.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.person.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonApiService {

    @GET("person/popular")
    suspend fun popularPersons(
        @Query("page") page: Int = 1
    ): GenericResponse<PersonResponse>

    @GET("trending/person/week")
    suspend fun trendingPersons(
        @Query("page") page: Int = 1
    ): GenericResponse<PersonResponse>

    @GET("person/{person_id}")
    suspend fun personDetails(
        @Path("person_id") personId: Int
    ): GenericResponse<PersonDetails>

    @GET("person/{person_id}/images")
    suspend fun personImage(
        @Path("person_id") personId: Int
    ): GenericResponse<PersonImage>

    @GET("person/{person_id}/movie_credits")
    suspend fun movieCredits(
        @Path("person_id") personId: Int
    ): GenericResponse<MovieCredits>

    @GET("person/{person_id}/tv_credits")
    suspend fun tvShowsCredits(
        @Path("person_id") personId: Int
    ): GenericResponse<TvShowsCredits>

}