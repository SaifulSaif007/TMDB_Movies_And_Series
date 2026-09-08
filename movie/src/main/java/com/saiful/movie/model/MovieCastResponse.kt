package com.saiful.movie.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieCastResponse(
    val id: Int? = 0,
    val cast: List<Cast>
)