package com.saiful.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("file_path")
    val filePath: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("aspect_ratio")
    val aspectRatio: Double? = null,
    val height: Int? = null,
    val width: Int? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null
)
