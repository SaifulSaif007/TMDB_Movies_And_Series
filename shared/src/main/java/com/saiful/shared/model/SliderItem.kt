package com.saiful.shared.model

data class SliderItem(
    val id: Int,
    val title: String?,
    val backdropPath: String?,
    val rating: Double? = null,
    val releaseDate: String? = null,
    val overview: String? = null
)
