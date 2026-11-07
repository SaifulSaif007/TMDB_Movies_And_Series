package com.saiful.shared.data.api

import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.model.auth.AccountDetailsResponse
import com.saiful.shared.model.auth.CreateSessionRequest
import com.saiful.shared.model.auth.DeleteSessionRequest
import com.saiful.shared.model.auth.DeleteSessionResponse
import com.saiful.shared.model.auth.RequestTokenResponse
import com.saiful.shared.model.auth.SessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {

    @GET("authentication/token/new")
    suspend fun createRequestToken(): GenericResponse<RequestTokenResponse>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Body body: CreateSessionRequest
    ): GenericResponse<SessionResponse>

    @GET("account")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): GenericResponse<AccountDetailsResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(
        @Body body: DeleteSessionRequest
    ): GenericResponse<DeleteSessionResponse>
}
