package com.saiful.tvshows.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDetails(
    val id: Int,
    @SerialName("air_date")
    val airDate: String,
    val name: String,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("season_number")
    val seasonNumber: Int,
    val episodes: List<Episode> = listOf()
)
