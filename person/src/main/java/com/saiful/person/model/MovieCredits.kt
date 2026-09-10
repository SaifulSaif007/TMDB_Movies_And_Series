package com.saiful.person.model

import com.saiful.shared.model.Movies
import kotlinx.serialization.Serializable

@Serializable
data class MovieCredits(
    val id: Int,
    val cast: List<Movies> = listOf(),
    val crew: List<Movies> = listOf()
)
