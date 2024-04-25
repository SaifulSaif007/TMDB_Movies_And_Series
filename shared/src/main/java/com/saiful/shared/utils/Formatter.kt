package com.saiful.shared.utils

import java.math.RoundingMode
import java.text.*


const val ONE_DECIMAL_FORMAT_PATTERN = "#.#"
const val TWO_DECIMAL_FORMAT_PATTERN = "#.##"


fun floatNumberFormatter(
    value: Float?,
    formatPattern: String = ONE_DECIMAL_FORMAT_PATTERN
): String {
    return try {
        val df = DecimalFormat(formatPattern)
        df.roundingMode = RoundingMode.HALF_EVEN
        df.format(value)
    } catch (e: Exception) {
        ""
    }
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