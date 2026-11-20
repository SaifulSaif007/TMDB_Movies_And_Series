package com.saiful.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaImagesResponse(
    val id: Int = 0,
    val backdrops: List<Image> = emptyList(),
    val posters: List<Image> = emptyList(),
    val logos: List<Image> = emptyList()
)
