package com.saiful.tvshows.model

import com.saiful.shared.model.TvShows
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowsResponse(
    val page: Int,
    val results: List<TvShows>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)