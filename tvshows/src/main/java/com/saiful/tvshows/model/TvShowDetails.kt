package com.saiful.tvshows.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDetails(
    val adult: Boolean? = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = "",
    @SerialName("created_by")
    val createdBy: List<CreatedBy?>? = null,
    @SerialName("episode_run_time")
    val episodeRunTime: List<Int?>? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    val genres: List<Genre?>? = null,
    val homepage: String? = null,
    val id: Int? = null,
    @SerialName("in_production")
    val inProduction: Boolean? = null,
    val languages: List<String?>? = null,
    val lastAirDate: String? = null,
    val name: String? = null,
    val networks: List<Network?>? = null,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int? = null,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int? = null,
    val originCountry: List<String?>? = null,
    val originalLanguage: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany?>? = null,
    val seasons: List<Season>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val type: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null,
    val videos: Videos? = null,
    @SerialName("spoken_languages")
    val spokenLanguage: List<SpokenLanguagesItem?>? = null,
)

@Serializable
data class CreatedBy(
    @SerialName("credit_id")
    val creditId: String? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null
)

@Serializable
data class Genre(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class Network(
    val id: Int? = null,
    @SerialName("logo_path")
    val logoPath: String? = null,
    val name: String? = null,
    @SerialName("origin_country")
    val originCountry: String? = null
)

@Serializable
data class ProductionCompany(
    val id: Int? = null,
    @SerialName("logo_path")
    val logoPath: String? = null,
    val name: String? = null,
    @SerialName("origin_country")
    val originCountry: String? = null
)

@Serializable
data class Season(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episode_count")
    val episodeCount: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("season_number")
    val seasonNumber: Int = 0
)

@Serializable
data class Videos(
    val results: List<VideoResult>? = null
)

@Serializable
data class SpokenLanguagesItem(
    val name: String? = null,
    @SerialName("english_name")
    val englishName: String? = null
)

@Serializable
data class VideoResult(
    @SerialName("video_id")
    val videoId: String?,
    val key: String? = null,
    val name: String? = null,
    val site: String? = null,
    val size: String? = null,
    val type: String? = null
)
