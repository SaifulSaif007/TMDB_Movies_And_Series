package com.saiful.tvshows.model

import com.squareup.moshi.Json

data class Episode(
    @field:Json(name = "air_date")
    val airDate: String,
    @field:Json(name = "episode_number")
    val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val runtime: Int? = 0,
    @field:Json(name = "season_number")
    val seasonNumber: Int? = 1,
    @field:Json(name = "still_path")
    val stillPath: String,
    @field:Json(name = "vote_average")
    val voteAverage: Double,
    @field:Json(name = "vote_count")
    val voteCount: Int? = 0
)
