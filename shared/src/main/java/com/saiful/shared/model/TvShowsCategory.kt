package com.saiful.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class TvShowsCategory(val value: String) {
    POPULAR("Popular"),
    TOP_RATED("Top Rated"),
    TRENDING("Trending"),
    ON_AIR("On Air")
}
