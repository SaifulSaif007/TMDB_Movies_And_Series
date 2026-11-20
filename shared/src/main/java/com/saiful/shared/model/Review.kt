package com.saiful.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponse(
    val id: Int = 0,
    val page: Int = 1,
    val results: List<ReviewItem> = emptyList(),
    @SerialName("total_pages")
    val totalPages: Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0
)

@Serializable
data class ReviewItem(
    val id: String = "",
    val author: String = "",
    @SerialName("author_details")
    val authorDetails: ReviewAuthorDetails? = null,
    val content: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
    val url: String = ""
)

@Serializable
data class ReviewAuthorDetails(
    val name: String = "",
    val username: String = "",
    @SerialName("avatar_path")
    val avatarPath: String? = null,
    val rating: Double? = null
)
