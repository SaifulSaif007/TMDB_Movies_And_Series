package com.saiful.shared.data.repository

import com.saiful.base.network.model.BaseResponse
import com.saiful.base_unit_test.BaseRepositoryTest
import com.saiful.base.session.SessionManager
import com.saiful.shared.data.api.AuthApiService
import com.saiful.shared.model.auth.AccountDetailsResponse
import com.saiful.shared.model.auth.AvatarResponse
import com.saiful.shared.model.auth.CreateSessionRequest
import com.saiful.shared.model.auth.DeleteSessionRequest
import com.saiful.shared.model.auth.DeleteSessionResponse
import com.saiful.shared.model.auth.GravatarAvatar
import com.saiful.shared.model.auth.RequestTokenResponse
import com.saiful.shared.model.auth.SessionResponse
import com.saiful.shared.model.auth.TmdbAvatar
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AuthRepositoryTest : BaseRepositoryTest() {

    private val authApiService: AuthApiService = mock()
    private val sessionManager: SessionManager = mock()
    private lateinit var authRepository: AuthRepository

    private val requestToken = "req_token_123"
    private val sessionId = "session_id_456"

    private val requestTokenResponse = RequestTokenResponse(
        success = true,
        requestToken = requestToken,
        expiresAt = "2026-11-10"
    )

    private val sessionResponse = SessionResponse(
        success = true,
        sessionId = sessionId
    )

    private val accountDetailsResponse = AccountDetailsResponse(
        id = 1001,
        name = "Saiful Islam",
        username = "saiful",
        includeAdult = false,
        avatar = AvatarResponse(
            tmdb = TmdbAvatar(avatarPath = "/avatar.png"),
            gravatar = GravatarAvatar(hash = "gravatar_hash_123")
        )
    )

    @Before
    override fun setup() {
        authRepository = AuthRepository(
            authApiService = authApiService,
            sessionManager = sessionManager
        )
    }

    @After
    override fun tearDown() {
        reset(authApiService, sessionManager)
    }

    @Test
    fun `verify createRequestToken returns success`() {
        runBlocking {
            whenever(authApiService.createRequestToken())
                .thenReturn(BaseResponse.Success(requestTokenResponse))

            val result = authRepository.createRequestToken()
            assert(result is BaseResponse.Success)
            assert((result as BaseResponse.Success).body.requestToken == requestToken)
        }
    }

    @Test
    fun `verify createRequestToken returns api error`() {
        runBlocking {
            whenever(authApiService.createRequestToken())
                .thenReturn(apiError)

            val result = authRepository.createRequestToken()
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify createSession returns success`() {
        runBlocking {
            val request = CreateSessionRequest(requestToken)
            whenever(authApiService.createSession(request))
                .thenReturn(BaseResponse.Success(sessionResponse))

            val result = authRepository.createSession(requestToken)
            assert(result is BaseResponse.Success)
            assert((result as BaseResponse.Success).body.sessionId == sessionId)
        }
    }

    @Test
    fun `verify createSession returns network error`() {
        runBlocking {
            val request = CreateSessionRequest(requestToken)
            whenever(authApiService.createSession(request))
                .thenReturn(networkError)

            val result = authRepository.createSession(requestToken)
            assert(result is BaseResponse.NetworkError)
        }
    }

    @Test
    fun `verify getAccountDetails returns success`() {
        runBlocking {
            whenever(authApiService.getAccountDetails(sessionId))
                .thenReturn(BaseResponse.Success(accountDetailsResponse))

            val result = authRepository.getAccountDetails(sessionId)
            assert(result is BaseResponse.Success)
            assert((result as BaseResponse.Success).body.username == "saiful")
        }
    }

    @Test
    fun `verify getAccountDetails returns api error`() {
        runBlocking {
            whenever(authApiService.getAccountDetails(sessionId))
                .thenReturn(apiError)

            val result = authRepository.getAccountDetails(sessionId)
            assert(result is BaseResponse.ApiError)
        }
    }

    @Test
    fun `verify saveSession saves session including gravatar hash`() {
        runBlocking {
            authRepository.saveSession(sessionId, accountDetailsResponse)

            verify(sessionManager).saveSession(
                sessionId = sessionId,
                accountId = accountDetailsResponse.id,
                username = accountDetailsResponse.username,
                name = accountDetailsResponse.name,
                avatarPath = "/avatar.png",
                gravatarHash = "gravatar_hash_123",
                includeAdult = false
            )
        }
    }

    @Test
    fun `verify logout deletes remote session and clears local session`() {
        runBlocking {
            whenever(sessionManager.currentSessionId).thenReturn(sessionId)
            whenever(authApiService.deleteSession(DeleteSessionRequest(sessionId)))
                .thenReturn(BaseResponse.Success(DeleteSessionResponse(success = true)))

            val result = authRepository.logout()
            assert(result)
            verify(authApiService).deleteSession(DeleteSessionRequest(sessionId))
            verify(sessionManager).clearSession()
        }
    }

    @Test
    fun `verify logout clears local session even if api call fails`() {
        runBlocking {
            whenever(sessionManager.currentSessionId).thenReturn(sessionId)
            whenever(authApiService.deleteSession(DeleteSessionRequest(sessionId)))
                .thenThrow(RuntimeException("Network failed"))

            val result = authRepository.logout()
            assert(result)
            verify(sessionManager).clearSession()
        }
    }
}
