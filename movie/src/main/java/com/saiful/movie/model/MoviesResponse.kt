package com.saiful.movie.model

import com.saiful.shared.model.Movies
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class MoviesResponse(
    val dates: DateRange? = null,
    val page: Int,
    val results: List<Movies>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)
