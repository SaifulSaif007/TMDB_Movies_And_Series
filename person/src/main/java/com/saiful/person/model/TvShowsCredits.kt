package com.saiful.person.model

import com.saiful.shared.model.TvShows
import kotlinx.serialization.Serializable

@Serializable
data class TvShowsCredits(
    val id: Int,
    val cast: List<TvShows> = listOf(),
    val crew: List<TvShows> = listOf()
)
