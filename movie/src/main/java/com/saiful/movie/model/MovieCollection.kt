package com.saiful.movie.model

import com.squareup.moshi.Json

data class MovieCollection(
    val id: Int,
    val name: String? = "",
    val overview: String? = "",
    @field:Json(name = "poster_path")
    val posterPath: String? = "",
    @field:Json(name = "backdrop_path")
    val backdropPath: String? = "",
    val parts: List<Movies>? = listOf()
)
