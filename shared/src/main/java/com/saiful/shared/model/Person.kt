package com.saiful.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val adult: Boolean? = false,
    val gender: Int = 0,
    val id: Int,
    @SerialName("known_for")
    val knownFor: List<KnownFor>? = null,
    @SerialName("known_for_department")
    val knownForDepartment: String? = "",
    val name: String,
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String? = ""
)

@Serializable
data class KnownFor(
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    val id: Int,
    @SerialName("media_type")
    val mediaType: String? = null,

    //series
    val title: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,

    //tv
    val name: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,

    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int? = 0
)

