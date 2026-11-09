package com.saiful.shared.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val favorite: Boolean
)

@Serializable
data class WatchlistRequest(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    val watchlist: Boolean
)

@Serializable
data class RatingRequest(
    val value: Float
)

@Serializable
data class AccountActionResponse(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String
)

@Serializable
data class AccountStateResponse(
    val id: Int,
    val favorite: Boolean = false,
    val watchlist: Boolean = false,
    val rated: RatedValue? = null
)

@Serializable
data class RatedValue(
    val value: Float
)

@Serializable
data class AccountMediaListResponse(
    val page: Int = 1,
    val results: List<AccountMediaItem> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
    @SerialName("total_results") val totalResults: Int = 0
)

@Serializable
data class AccountMediaItem(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    val overview: String? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("media_type") val mediaType: String? = null
)