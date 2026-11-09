package com.saiful.base.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val sessionId: String,
    val accountId: Int,
    val username: String,
    val name: String,
    val avatarPath: String? = null,
    val gravatarHash: String? = null,
    val includeAdult: Boolean = false
)
