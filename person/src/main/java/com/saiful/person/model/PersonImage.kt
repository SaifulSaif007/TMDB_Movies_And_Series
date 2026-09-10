package com.saiful.person.model

import com.saiful.shared.model.Image
import kotlinx.serialization.Serializable

@Serializable
data class PersonImage(
    val id: Int,
    val profiles: List<Image> = listOf()
)

