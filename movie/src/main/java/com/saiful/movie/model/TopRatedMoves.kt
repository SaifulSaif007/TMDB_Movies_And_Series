package com.saiful.movie.model

import com.squareup.moshi.Json

data class TopRatedMoves(
    val page: Int,
    val results: List<Movies>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    @field:Json(name = "total_results")
    val totalResults: Int
)