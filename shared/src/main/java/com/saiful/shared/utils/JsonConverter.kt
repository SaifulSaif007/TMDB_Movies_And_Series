package com.saiful.shared.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonConverter {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    inline fun <reified T> fromJson(jsonString: String): T? {
        return runCatching {
            json.decodeFromString<T>(jsonString)
        }.getOrNull()
    }

    inline fun <reified T> toJson(value: T): String {
        return json.encodeToString(value)
    }

    inline fun <reified T> fromJsonList(jsonString: String): List<T>? {
        return runCatching {
            json.decodeFromString<List<T>>(jsonString)
        }.getOrNull()
    }

    inline fun <reified T> toJsonList(value: List<T>): String {
        return json.encodeToString(value)
    }
}
