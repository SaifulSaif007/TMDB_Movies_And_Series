package com.saiful.shared.model

import com.squareup.moshi.Json

class Movies(
    val adult: Boolean? = null,
    @field:Json(name = "backdrop_path")
    val backdropPath: String? = null,
    val id: Int,
    @field:Json(name = "original_language")
    val originalLanguage: String? = null,
    @field:Json(name = "original_title")
    val originalTitle: String? = null,
    @field:Json(name = "overview")
    val overview: String? = null,
    val popularity: Double? = null,
    @field:Json(name = "poster_path")
    val posterPath: String? = null,
    @field:Json(name = "release_date")
    val releaseDate: String? = null,
    val title: String? = null,
    @field:Json(name = "vote_average")
    val voteAverage: Double? = null,
    @field:Json(name = "vote_count")
    val voteCount: Int? = null
)