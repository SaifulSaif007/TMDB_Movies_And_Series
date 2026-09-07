package com.saiful.movie.model

import com.saiful.shared.model.Movies
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesResponse(
    val dates: DateRange,
    val page: Int,
    val results: List<Movies>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    @field:Json(name = "total_results")
    val totalResults: Int
)