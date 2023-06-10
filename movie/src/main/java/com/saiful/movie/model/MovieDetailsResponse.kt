package com.saiful.movie.model

import com.squareup.moshi.Json

data class MovieDetailsResponse(

	val id: Int? = null,
	@field:Json(name="original_language")
	val originalLanguage: String? = null,
	@field:Json(name="imdb_id")
	val imdbId: String? = null,
	val videos: Videos? = null,
	val video: Boolean? = null,
	val title: String? = null,
	@field:Json(name="backdrop_path")
	val backdropPath: String? = null,
	val revenue: Double? = null,
	val genres: List<GenresItem?>? = null,
	val popularity: Double? = null,
	@field:Json(name="production_countries")
	val productionCountries: List<ProductionCountriesItem?>? = null,
	@field:Json(name="vote_count")
	val voteCount: Int? = null,
	val budget: Int? = null,
	val overview: String? = null,
	@field:Json(name="original_title")
	val originalTitle: String? = null,
	val runtime: Int? = null,
	@field:Json(name="poster_path")
	val posterPath: String? = null,
	@field:Json(name="spoken_languages")
	val spokenLanguages: List<SpokenLanguagesItem?>? = null,
	@field:Json(name="production_companies")
	val productionCompanies: List<ProductionCompaniesItem?>? = null,
	@field:Json(name="release_date")
	val releaseDate: String? = null,
	@field:Json(name="vote_average")
	val voteAverage: Double? = null,
	@field:Json(name="belongs_to_collection")
	val belongsToCollection: BelongsToCollection? = null,
	@field:Json(name="tagline")
	val tagline: String? = null,
	val adult: Boolean? = null,
	val homepage: String? = null,
	val status: String? = null
)

data class ResultsItem(
	val id: String? = null,
	val site: String? = null,
	val size: Int? = null,
	val name: String? = null,
	val official: Boolean? = null,
	val type: String? = null,
	@field:Json(name="published_at")
	val publishedAt: String? = null,
	@field:Json(name="key")
	val key: String? = null
)

data class ProductionCountriesItem(
	@Json(name="name")
	val name: String? = null
)

data class SpokenLanguagesItem(
	val name: String? = null,
	@field:Json(name="english_name")
	val englishName: String? = null
)

data class BelongsToCollection(
	val id: Int? = null,
	@field:Json(name="backdrop_path")
	val backdropPath: String? = null,
	@field:Json(name="name")
	val name: String? = null,
	@field:Json(name="poster_path")
	val posterPath: String? = null
)

data class GenresItem(
	val name: String? = null,
	val id: Int? = null
)

data class Videos(
	@field:Json(name="results")
	val results: List<ResultsItem>? = null
)

data class ProductionCompaniesItem(
	val id: Int? = null,
	@field:Json(name="logo_path")
	val logoPath: String? = null,
	val name: String? = null,
	@Json(name="origin_country")
	val originCountry: String? = null
)
