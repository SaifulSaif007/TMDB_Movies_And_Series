package com.saiful.person.model

import com.squareup.moshi.Json

data class Person(
    val adult: Boolean? = false,
    val gender: Int = 0,
    val id: Int,
    @field:Json(name = "known_for")
    val knownFor: List<KnownFor>? = null,
    @field:Json(name = "known_for_department")
    val knownForDepartment: String? = "",
    val name: String,
    val popularity: Double,
    @field:Json(name = "profile_path")
    val profilePath: String
)


data class KnownFor(
    @field:Json(name = "backdrop_path")
    val backdropPath: String,
    @field:Json(name = "first_air_date")
    val firstAirDate: String,
    val id: Int,
    @field:Json(name = "media_type")
    val mediaType: String,
    val name: String,
    @field:Json(name = "original_name")
    val originalName: String,
    val overview: String,
    @field:Json(name = "poster_path")
    val posterPath: String,
    @field:Json(name = "vote_average")
    val voteAverage: Double,
    @field:Json(name = "vote_count")
    val voteCount: Int? = 0
)
