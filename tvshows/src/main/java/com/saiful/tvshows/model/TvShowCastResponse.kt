package com.saiful.tvshows.model

import kotlinx.serialization.Serializable

@Serializable
data class TvShowCastResponse(
    val id: Int? = 0,
    val cast: List<Cast>
)
