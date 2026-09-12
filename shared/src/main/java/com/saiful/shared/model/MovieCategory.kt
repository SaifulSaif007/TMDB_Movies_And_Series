package com.saiful.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class MovieCategory(val value: String) {
    POPULAR("Popular"),
    TOP_RATED("Top Rated"),
    NOW_PLAYING("Now Playing"),
    UPCOMING("Upcoming")
}
