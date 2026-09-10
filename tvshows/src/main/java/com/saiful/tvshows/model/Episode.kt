package com.saiful.tvshows.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    @SerialName("air_date")
    val airDate: String,
    @SerialName("episode_number")
    val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val runtime: Int? = 0,
    @SerialName("season_number")
    val seasonNumber: Int? = 1,
    @SerialName("still_path")
    val stillPath: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int? = 0
)
