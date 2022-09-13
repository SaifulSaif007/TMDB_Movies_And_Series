package com.saiful.tvshows.model

import com.squareup.moshi.Json

data class Cast(
    val adult: Boolean? = false,
    val gender: Int = 0,
    val id: Int,
    @field:Json(name = "known_for_department")
    val knownFor: String? = "",
    val name: String? = "",
    @field:Json(name = "original_name")
    val originalName: String? = "",
    val popularity: Double? = 0.0,
    @field:Json(name = "profile_path")
    val profilePath: String?,
    val roles: List<Roles>,
    @field:Json(name = "total_episode_count")
    val totalEpisode: Int,
    val order: Int? = -1
) {

    data class Roles(
        val character: String,
        @field:Json(name = "episode_count")
        val episodeCount: Int
    )
}
