package com.saiful.tvshows.model

import com.squareup.moshi.Json

data class TvShowDetails(
    val adult: Boolean? = false,
    @field:Json(name = "backdrop_path")
    val backdropPath: String? = "",
    @field:Json(name = "created_by")
    val createdBy: List<CreatedBy?>? = null,
    @field:Json(name = "episode_run_time")
    val episodeRunTime: List<Int?>? = null,
    @field:Json(name = "first_air_date")
    val firstAirDate: String? = null,
    val genres: List<Genre?>? = null,
    val homepage: String? = null,
    val id: Int? = null,
    @field:Json(name = "in_production")
    val inProduction: Boolean? = null,
    val languages: List<String?>? = null,
    val lastAirDate: String? = null,
    val name: String? = null,
    val networks: List<Network?>? = null,
    @field:Json(name = "number_of_episodes")
    val numberOfEpisodes: Int? = null,
    @field:Json(name = "number_of_seasons")
    val numberOfSeasons: Int? = null,
    val originCountry: List<String?>? = null,
    val originalLanguage: String? = null,
    @field:Json(name = "original_name")
    val originalName: String? = null,
    @field:Json(name = "overview")
    val overview: String? = null,
    @field:Json(name = "popularity")
    val popularity: Double? = null,
    @field:Json(name = "poster_path")
    val posterPath: String? = null,
    @field:Json(name = "production_companies")
    val productionCompanies: List<ProductionCompany?>? = null,
    val seasons: List<Season?>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val type: String? = null,
    @field:Json(name = "vote_average")
    val voteAverage: Double? = null,
    @field:Json(name = "vote_count")
    val voteCount: Int? = null,
    val videos: Videos? = null,
    @field:Json(name = "spoken_languages")
    val spokenLanguage:List<SpokenLanguagesItem?>? = null,
) {
    data class CreatedBy(
        @field:Json(name = "credit_id")
        val creditId: String? = null,
        @field:Json(name = "gender")
        val gender: Int? = null,
        val id: Int? = null,
        val name: String? = null,
        @field:Json(name = "profile_path")
        val profilePath: Any? = null
    )

    data class Genre(
        val id: Int? = null,
        val name: String? = null
    )

    data class Network(
        val id: Int? = null,
        @field:Json(name = "logo_path")
        val logoPath: String? = null,
        val name: String? = null,
        @field:Json(name = "origin_country")
        val originCountry: String? = null
    )

    data class ProductionCompany(
        val id: Int? = null,
        @field:Json(name = "logo_path")
        val logoPath: String? = null,
        val name: String? = null,
        @field:Json(name = "origin_country")
        val originCountry: String? = null
    )

    data class Season(
        @field:Json(name = "air_date")
        val airDate: String? = null,
        @field:Json(name = "episode_count")
        val episodeCount: Int? = null,
        val id: Int? = null,
        val name: String? = null,
        val overview: String? = null,
        @field:Json(name = "poster_path")
        val posterPath: String? = null,
        @field:Json(name = "season_number")
        val seasonNumber: Int? = null
    )

    data class Videos(
        val results: List<VideoResult>? = null
    )

    data class SpokenLanguagesItem(
        val name: String? = null,
        @field:Json(name="english_name")
        val englishName: String? = null
    )

    data class VideoResult(
        val video_id: String?,
        val key: String? = null,
        val name: String? = null,
        val site: String? = null,
        val size: String? = null,
        val type: String? = null
    )
}
