package com.saiful.base.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GenericError (
    val status_code: String,
    val status_message: String
)
