package com.saiful.person.model

import com.squareup.moshi.Json

data class PersonDetails(
    val adult: Boolean? = false,
    @field:Json(name = "also_known_as")
    val knowAs: List<String>? = listOf(),
    val biography: String = "",
    val birthday: String = "",
    val gender: Int,
    val id: Int,
    @field:Json(name = "known_for_department")
    val department: String = "",
    val name: String,
    @field:Json(name = "place_of_birth")
    val birthPlace: String = "",
    @field:Json(name = "profile_path")
    val profilePath: String = ""
)
