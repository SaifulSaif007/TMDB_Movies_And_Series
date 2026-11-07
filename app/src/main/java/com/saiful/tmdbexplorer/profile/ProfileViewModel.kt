package com.saiful.tmdbexplorer.profile

import androidx.lifecycle.viewModelScope
import com.saiful.base.model.UserSession
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.shared.data.repository.AuthRepository
import com.saiful.tmdbexplorer.auth.AuthDeepLinkManager
import com.saiful.tmdbexplorer.auth.AuthDeepLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val userSession: UserSession? = null,
    val isLoading: Boolean = false
)

sealed interface ProfileUiEvent {
    data class LaunchCustomTab(val requestToken: String) : ProfileUiEvent
    data class ShowMessage(val message: String) : ProfileUiEvent
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authDeepLinkManager: AuthDeepLinkManager
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ProfileUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        observeUserSession()
        observeDeepLinkEvents()
    }

    private fun observeUserSession() {
        viewModelScope.launch {
            authRepository.userSession.collect { session ->
                _uiState.update { it.copy(userSession = session) }
            }
        }
    }

    private fun observeDeepLinkEvents() {
        viewModelScope.launch {
            authDeepLinkManager.deepLinkEvents.collect { event ->
                when (event) {
                    is AuthDeepLinkResult.Approved -> {
                        exchangeTokenForSession(event.requestToken)
                    }
                    is AuthDeepLinkResult.Denied -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.emit(ProfileUiEvent.ShowMessage("Authentication was denied on TMDB."))
                    }
                }
            }
        }
    }

    fun loginWithTmdb() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = authRepository.createRequestToken()) {
                is BaseResponse.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.LaunchCustomTab(response.body.requestToken))
                }
                is BaseResponse.ApiError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage(response.errorBody.status_message))
                }
                is BaseResponse.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Network error. Please check your connection."))
                }
                is BaseResponse.UnknownError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Failed to initiate TMDB login."))
                }
            }
        }
    }

    private fun exchangeTokenForSession(requestToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val sessionResponse = authRepository.createSession(requestToken)) {
                is BaseResponse.Success -> {
                    val sessionId = sessionResponse.body.sessionId
                    if (!sessionId.isNullOrBlank()) {
                        fetchAccountDetails(sessionId)
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.emit(ProfileUiEvent.ShowMessage("Could not retrieve session ID."))
                    }
                }
                is BaseResponse.ApiError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage(sessionResponse.errorBody.status_message))
                }
                is BaseResponse.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Network error during authentication."))
                }
                is BaseResponse.UnknownError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Failed to complete session authorization."))
                }
            }
        }
    }

    private fun fetchAccountDetails(sessionId: String) {
        viewModelScope.launch {
            when (val accountResponse = authRepository.getAccountDetails(sessionId)) {
                is BaseResponse.Success -> {
                    val account = accountResponse.body
                    authRepository.saveSession(sessionId, account)
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Welcome back, ${account.username}!"))
                }
                is BaseResponse.ApiError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Failed to load account details."))
                }
                is BaseResponse.NetworkError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Network error while loading profile."))
                }
                is BaseResponse.UnknownError -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(ProfileUiEvent.ShowMessage("Could not fetch account info."))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.logout()
            _uiState.update { it.copy(isLoading = false) }
            _uiEvent.emit(ProfileUiEvent.ShowMessage("Logged out successfully."))
        }
    }
}
