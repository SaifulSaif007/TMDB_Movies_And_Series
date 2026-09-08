package com.saiful.base.network.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class GenericError (
    val status_code: String,
    val status_message: String
)
