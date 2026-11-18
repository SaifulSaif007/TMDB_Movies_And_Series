package com.saiful.shared.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchResult(
    val id: Int = 0,
    @SerialName("media_type") val mediaType: String = "",
    val title: String? = null,
    val name: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    val overview: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    val popularity: Double? = null
) {
    val displayTitle: String get() = title ?: name ?: ""
    val displayImagePath: String? get() = posterPath ?: profilePath
    val displayDate: String? get() = releaseDate ?: firstAirDate
}

@Serializable
data class MultiSearchResponse(
    val page: Int = 1,
    val results: List<MultiSearchResult> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
    @SerialName("total_results") val totalResults: Int = 0
)
