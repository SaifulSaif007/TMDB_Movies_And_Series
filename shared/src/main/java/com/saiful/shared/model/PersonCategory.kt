package com.saiful.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class PersonCategory(val value: String) {
    POPULAR("Popular"),
    TRENDING("Trending")
}
