package com.saiful.person.model

import com.squareup.moshi.Json

data class PersonImage(
    val id: Int,
    val profiles: List<Image> = listOf()
)

data class Image(
    @field:Json(name = "file_path")
    val filePath: String = "",
    @field:Json(name = "vote_average")
    val voteAverage: Double = 0.0
)
