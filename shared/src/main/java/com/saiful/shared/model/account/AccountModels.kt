package com.saiful.shared.model.account

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.floatOrNull

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

@Serializable(with = RatedValueSerializer::class)
data class RatedValue(
    val value: Float
)

object RatedValueSerializer : KSerializer<RatedValue?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("RatedValue", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: RatedValue?) {
        if (value == null) {
            encoder.encodeBoolean(false)
        } else {
            encoder.encodeFloat(value.value)
        }
    }

    override fun deserialize(decoder: Decoder): RatedValue? {
        val jsonDecoder = decoder as? JsonDecoder ?: return null
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> {
                val floatVal = element.floatOrNull
                if (floatVal != null) RatedValue(floatVal) else null
            }
            is JsonObject -> {
                val floatVal = element["value"]?.let {
                    if (it is JsonPrimitive) it.floatOrNull else null
                }
                if (floatVal != null) RatedValue(floatVal) else null
            }
            else -> null
        }
    }
}

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
