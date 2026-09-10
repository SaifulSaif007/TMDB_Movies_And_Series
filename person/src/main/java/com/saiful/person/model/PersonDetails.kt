package com.saiful.person.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonDetails(
    val adult: Boolean? = false,
    @SerialName("also_known_as")
    val knowAs: List<String>? = listOf(),
    val biography: String = "",
    val birthday: String = "",
    val gender: Int,
    val id: Int,
    @SerialName("known_for_department")
    val department: String = "",
    val name: String,
    @SerialName("place_of_birth")
    val birthPlace: String = "",
    @SerialName("profile_path")
    val profilePath: String = ""
)
