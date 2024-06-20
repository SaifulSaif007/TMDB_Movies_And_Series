package com.saiful.shared.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object JsonConverter {
    val moshi = Moshi.Builder().build()

    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        return moshi.adapter(clazz).fromJson(json)
    }

    fun <T> toJson(value: T, clazz: Class<T>): String {
        return moshi.adapter(clazz).toJson(value)
    }

    inline fun <reified T> fromJsonList(json: String): List<T>? =
        moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
            .fromJson(json)

    inline fun <reified T> toJsonList(value: List<T>): String =
        moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java).rawType)
            .toJson(value)
}