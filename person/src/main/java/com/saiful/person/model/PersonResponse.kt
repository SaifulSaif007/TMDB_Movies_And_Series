package com.saiful.person.model

import com.saiful.shared.model.Person
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonResponse(
    val page: Int,
    val results: List<Person>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)
