package com.saiful.tmdbexplorer.auth

import android.net.Uri
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface AuthDeepLinkResult {
    data class Approved(val requestToken: String) : AuthDeepLinkResult
    data object Denied : AuthDeepLinkResult
}

@Singleton
class AuthDeepLinkManager @Inject constructor() {

    private val _deepLinkEvents = MutableSharedFlow<AuthDeepLinkResult>(extraBufferCapacity = 1)
    val deepLinkEvents = _deepLinkEvents.asSharedFlow()

    fun handleDeepLink(uri: Uri?): Boolean {
        if (uri == null) return false
        if (uri.scheme == "tmdbexplorer" && uri.host == "auth_callback") {
            val approved = uri.getBooleanQueryParameter("approved", false)
            val denied = uri.getBooleanQueryParameter("denied", false)
            val requestToken = uri.getQueryParameter("request_token")

            if (approved && !requestToken.isNullOrBlank()) {
                _deepLinkEvents.tryEmit(AuthDeepLinkResult.Approved(requestToken))
                return true
            } else if (denied) {
                _deepLinkEvents.tryEmit(AuthDeepLinkResult.Denied)
                return true
            }
        }
        return false
    }
}
