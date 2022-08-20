package com.saiful.base.util

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

fun floatNumberFormatter(value: Float): String {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_EVEN
    return df.format(value)
}

fun Long.formatToShortNumber(): String {
    return when {
        this >= 1000000000 -> String.format("%.1fB", this / 1000000000.0)
        this >= 1000000 -> String.format("%.1fM", this / 1000000.0)
        this >= 1000 -> String.format("%.1fK", this / 1000.0)
        else -> this.toString()
    }
}

fun String.formatDate(): String {
    val apiFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat = SimpleDateFormat("dd MMM, yyyy")
    return try {
        val dateFormat = apiFormat.parse(this)
        outputFormat.format(dateFormat!!)
    } catch (e: ParseException) {
        this
    }
}