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

