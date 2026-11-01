package com.saiful.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("file_path")
    val filePath: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0
)
