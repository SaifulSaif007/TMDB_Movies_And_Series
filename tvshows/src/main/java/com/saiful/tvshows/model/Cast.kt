package com.saiful.tvshows.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cast(
    val adult: Boolean? = false,
    val gender: Int = 0,
    val id: Int,
    @SerialName("known_for_department")
    val knownFor: String? = "",
    val name: String? = "",
    @SerialName("original_name")
    val originalName: String? = "",
    val popularity: Double? = 0.0,
    @SerialName("profile_path")
    val profilePath: String?,
    val roles: List<Roles>,
    @SerialName("total_episode_count")
    val totalEpisode: Int,
    val order: Int? = -1
) {

    @Serializable
    data class Roles(
        val character: String,
        @SerialName("episode_count")
        val episodeCount: Int
    )
}
