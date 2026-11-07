package com.saiful.shared.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTokenResponse(
    val success: Boolean,
    @SerialName("request_token") val requestToken: String,
    @SerialName("expires_at") val expiresAt: String
)

@Serializable
data class CreateSessionRequest(
    @SerialName("request_token") val requestToken: String
)

@Serializable
data class SessionResponse(
    val success: Boolean,
    @SerialName("session_id") val sessionId: String? = null,
    @SerialName("status_code") val statusCode: Int? = null,
    @SerialName("status_message") val statusMessage: String? = null
)

@Serializable
data class AccountDetailsResponse(
    val id: Int,
    val name: String = "",
    val username: String = "",
    @SerialName("include_adult") val includeAdult: Boolean = false,
    val avatar: AvatarResponse? = null
)

@Serializable
data class AvatarResponse(
    val tmdb: TmdbAvatar? = null,
    val gravatar: GravatarAvatar? = null
)

@Serializable
data class TmdbAvatar(
    @SerialName("avatar_path") val avatarPath: String? = null
)

@Serializable
data class GravatarAvatar(
    val hash: String? = null
)

@Serializable
data class DeleteSessionRequest(
    @SerialName("session_id") val sessionId: String
)

@Serializable
data class DeleteSessionResponse(
    val success: Boolean
)
