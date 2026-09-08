package com.saiful.movie.model

import com.saiful.shared.model.Movies
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCollection(
    val id: Int,
    val name: String? = "",
    val overview: String? = "",
    @SerialName("poster_path")
    val posterPath: String? = "",
    @SerialName("backdrop_path")
    val backdropPath: String? = "",
    val parts: List<Movies>? = listOf()
)
