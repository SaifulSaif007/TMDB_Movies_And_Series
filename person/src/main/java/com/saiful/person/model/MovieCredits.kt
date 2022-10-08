package com.saiful.person.model

import com.saiful.shared.model.Movies

data class MovieCredits(
    val id: Int,
    val cast: List<Movies> = listOf(),
    val crew: List<Movies> = listOf()
)
