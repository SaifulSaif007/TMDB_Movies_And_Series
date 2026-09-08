package com.saiful.movie.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class DateRange(
    val maximum : String,
    val minimum : String
)