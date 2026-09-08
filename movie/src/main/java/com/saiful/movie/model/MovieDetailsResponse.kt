package com.saiful.movie.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(

	val id: Int? = null,
	@SerialName("original_language")
	val originalLanguage: String? = null,
	@SerialName("imdb_id")
	val imdbId: String? = null,
	val videos: Videos? = null,
	val video: Boolean? = null,
	val title: String? = null,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	val revenue: Double? = null,
	val genres: List<GenresItem?>? = null,
	val popularity: Double? = null,
	@SerialName("production_countries")
	val productionCountries: List<ProductionCountriesItem?>? = null,
	@SerialName("vote_count")
	val voteCount: Int? = null,
	val budget: Int? = null,
	val overview: String? = null,
	@SerialName("original_title")
	val originalTitle: String? = null,
	val runtime: Int? = null,
	@SerialName("poster_path")
	val posterPath: String? = null,
	@SerialName("spoken_languages")
	val spokenLanguages: List<SpokenLanguagesItem?>? = null,
	@SerialName("production_companies")
	val productionCompanies: List<ProductionCompaniesItem?>? = null,
	@SerialName("release_date")
	val releaseDate: String? = null,
	@SerialName("vote_average")
	val voteAverage: Double? = null,
	@SerialName("belongs_to_collection")
	val belongsToCollection: BelongsToCollection? = null,
	@SerialName("tagline")
	val tagline: String? = null,
	val adult: Boolean? = null,
	val homepage: String? = null,
	val status: String? = null
)

@Serializable
data class ResultsItem(
	val id: String? = null,
	val site: String? = null,
	val size: Int? = null,
	val name: String? = null,
	val official: Boolean? = null,
	val type: String? = null,
	@SerialName("published_at")
	val publishedAt: String? = null,
	@SerialName("key")
	val key: String? = null
)

@Serializable
data class ProductionCountriesItem(
	@SerialName("name")
	val name: String? = null
)

@Serializable
data class SpokenLanguagesItem(
	val name: String? = null,
	@SerialName("english_name")
	val englishName: String? = null
)

@Serializable
data class BelongsToCollection(
	val id: Int? = null,
	@SerialName("backdrop_path")
	val backdropPath: String? = null,
	@SerialName("name")
	val name: String? = null,
	@SerialName("poster_path")
	val posterPath: String? = null
)

@Serializable
data class GenresItem(
	val name: String? = null,
	val id: Int? = null
)

@Serializable
data class Videos(
	@SerialName("results")
	val results: List<ResultsItem>? = null
)

@Serializable
data class ProductionCompaniesItem(
	val id: Int? = null,
	@SerialName("logo_path")
	val logoPath: String? = null,
	val name: String? = null,
	@SerialName("origin_country")
	val originCountry: String? = null
)

