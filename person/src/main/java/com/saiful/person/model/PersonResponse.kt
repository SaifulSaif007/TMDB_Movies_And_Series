package com.saiful.person.model

import com.squareup.moshi.Json

data class PersonResponse(
    val page: Int,
    val results: List<Person>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    @field:Json(name = "total_results")
    val totalResults: Int
)