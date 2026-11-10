package com.saiful.shared.utils

import com.saiful.base.model.UserSession

inline fun <T> executeIfLoggedIn(
    session: UserSession?,
    action: (UserSession) -> T,
    onUnauthenticated: () -> Unit
): T? {
    return if (session != null) {
        action(session)
    } else {
        onUnauthenticated()
        null
    }
}
