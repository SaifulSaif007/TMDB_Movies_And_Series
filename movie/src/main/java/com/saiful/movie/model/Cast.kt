package com.saiful.movie.model

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
    @SerialName("cast_id")
    val castId: Int? = 0,
    val character: String,
    val order: Int? = -1
)
