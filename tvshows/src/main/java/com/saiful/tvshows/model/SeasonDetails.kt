package com.saiful.tvshows.model

import com.squareup.moshi.Json

data class SeasonDetails(
    val id: Int,
    @field:Json(name = "air_date")
    val airDate: String,
    val name: String,
    val overview: String,
    @field:Json(name = "poster_path")
    val posterPath: String,
    @field:Json(name = "season_number")
    val seasonNumber: Int,
    val episodes: List<Episode> = listOf()
)

data class Episode(
    @field:Json(name = "air_date")
    val airDate: String,
    @field:Json(name = "episode_number")
    val episodeNumber: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val runtime: Int,
    @field:Json(name = "season_number")
    val seasonNumber: Int,
    @field:Json(name = "still_path")
    val stillPath: String,
    @field:Json(name = "vote_average")
    val voteAverage: Double,
    @field:Json(name = "vote_count")
    val voteCount: Int
)
