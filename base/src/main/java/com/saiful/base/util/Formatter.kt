package com.saiful.base.util

import java.math.RoundingMode
import java.text.DecimalFormat

fun floatNumberFormatter(value: Float) : String{
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.HALF_EVEN
    return df.format(value)
}