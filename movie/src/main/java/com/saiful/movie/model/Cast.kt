package com.saiful.movie.model

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
    @field:Json(name = "cast_id")
    val castId: Int? = 0,
    val character: String,
    val order: Int? = -1
)