package com.saiful.shared.data.repository

import com.saiful.base.model.UserSession
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.base.session.SessionManager
import com.saiful.shared.data.api.AuthApiService
import com.saiful.shared.model.auth.AccountDetailsResponse
import com.saiful.shared.model.auth.CreateSessionRequest
import com.saiful.shared.model.auth.DeleteSessionRequest
import com.saiful.shared.model.auth.RequestTokenResponse
import com.saiful.shared.model.auth.SessionResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager
) {
    val userSession: Flow<UserSession?> = sessionManager.userSession
    val isLoggedIn: Flow<Boolean> = sessionManager.isLoggedIn

    suspend fun createRequestToken(): GenericResponse<RequestTokenResponse> {
        return authApiService.createRequestToken()
    }

    suspend fun createSession(requestToken: String): GenericResponse<SessionResponse> {
        return authApiService.createSession(CreateSessionRequest(requestToken))
    }

    suspend fun getAccountDetails(sessionId: String): GenericResponse<AccountDetailsResponse> {
        return authApiService.getAccountDetails(sessionId)
    }

    suspend fun saveSession(sessionId: String, account: AccountDetailsResponse) {
        val avatarPath = account.avatar?.tmdb?.avatarPath
        val gravatarHash = account.avatar?.gravatar?.hash
        sessionManager.saveSession(
            sessionId = sessionId,
            accountId = account.id,
            username = account.username,
            name = account.name,
            avatarPath = avatarPath,
            gravatarHash = gravatarHash,
            includeAdult = account.includeAdult
        )
    }

    suspend fun logout(): Boolean {
        val currentSessionId = sessionManager.currentSessionId
        if (!currentSessionId.isNullOrBlank()) {
            try {
                authApiService.deleteSession(DeleteSessionRequest(currentSessionId))
            } catch (_: Exception) {
                // Ignore API deletion error during local logout
            }
        }
        sessionManager.clearSession()
        return true
    }
}
