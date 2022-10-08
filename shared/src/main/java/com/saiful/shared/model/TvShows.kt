package com.saiful.shared.model

import com.squareup.moshi.Json

data class TvShows(
    val adult: Boolean? = null,
    @field:Json(name = "backdrop_path")
    val backdropPath: String? = null,
    val id: Int,
    val name: String? = "",
    @field:Json(name = "original_language")
    val originalLanguage: String? = "",
    @field:Json(name = "original_name")
    val originalName: String? = "",
    val overview: String? = null,
    @field:Json(name = "poster_path")
    val posterPath: String? = null,
    @field:Json(name = "media_type")
    val mediaType: String? = "",
    val popularity: Double? = null,
    @field:Json(name = "first_air_date")
    val firstAirDate: String? = null,
    @field:Json(name = "vote_average")
    val voteAverage: Double? = null,
    @field:Json(name = "vote_count")
    val voteCount: Int? = null
)
