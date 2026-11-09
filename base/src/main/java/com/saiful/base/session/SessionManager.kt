package com.saiful.base.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.saiful.base.model.UserSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tmdb_user_session")

@Singleton
class SessionManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _cachedSessionId = MutableStateFlow<String?>(null)
    val cachedSessionId = _cachedSessionId.asStateFlow()

    val currentSessionId: String?
        get() = _cachedSessionId.value

    companion object {
        private val KEY_SESSION_ID = stringPreferencesKey("key_session_id")
        private val KEY_ACCOUNT_ID = intPreferencesKey("key_account_id")
        private val KEY_USERNAME = stringPreferencesKey("key_username")
        private val KEY_NAME = stringPreferencesKey("key_name")
        private val KEY_AVATAR_PATH = stringPreferencesKey("key_avatar_path")
        private val KEY_GRAVATAR_HASH = stringPreferencesKey("key_gravatar_hash")
        private val KEY_INCLUDE_ADULT = booleanPreferencesKey("key_include_adult")
    }

    init {
        scope.launch {
            context.dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .collect { prefs ->
                    _cachedSessionId.value = prefs[KEY_SESSION_ID]
                }
        }
    }

    val userSession: Flow<UserSession?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sessionId = preferences[KEY_SESSION_ID]
            val accountId = preferences[KEY_ACCOUNT_ID]
            val username = preferences[KEY_USERNAME]
            val name = preferences[KEY_NAME]
            val avatarPath = preferences[KEY_AVATAR_PATH]
            val gravatarHash = preferences[KEY_GRAVATAR_HASH]
            val includeAdult = preferences[KEY_INCLUDE_ADULT] ?: false

            if (sessionId != null && accountId != null && username != null) {
                UserSession(
                    sessionId = sessionId,
                    accountId = accountId,
                    username = username,
                    name = name ?: "",
                    avatarPath = avatarPath,
                    gravatarHash = gravatarHash,
                    includeAdult = includeAdult
                )
            } else {
                null
            }
        }

    val isLoggedIn: Flow<Boolean> = userSession.map { it != null }

    suspend fun saveSession(
        sessionId: String,
        accountId: Int,
        username: String,
        name: String,
        avatarPath: String?,
        gravatarHash: String?,
        includeAdult: Boolean
    ) {
        _cachedSessionId.value = sessionId
        context.dataStore.edit { prefs ->
            prefs[KEY_SESSION_ID] = sessionId
            prefs[KEY_ACCOUNT_ID] = accountId
            prefs[KEY_USERNAME] = username
            prefs[KEY_NAME] = name
            if (avatarPath != null) {
                prefs[KEY_AVATAR_PATH] = avatarPath
            } else {
                prefs.remove(KEY_AVATAR_PATH)
            }
            if (gravatarHash != null) {
                prefs[KEY_GRAVATAR_HASH] = gravatarHash
            } else {
                prefs.remove(KEY_GRAVATAR_HASH)
            }
            prefs[KEY_INCLUDE_ADULT] = includeAdult
        }
    }

    suspend fun clearSession() {
        _cachedSessionId.value = null
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
