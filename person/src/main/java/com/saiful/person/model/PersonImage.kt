package com.saiful.person.model

import com.saiful.shared.model.Image

data class PersonImage(
    val id: Int,
    val profiles: List<Image> = listOf()
)

