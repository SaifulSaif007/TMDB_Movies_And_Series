package com.saiful.tvshows.model

import com.squareup.moshi.Json

data class TvShowsResponse(
    val page: Int,
    val results: List<TvShows>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    @field:Json(name = "total_results")
    val totalResults: Int
)
