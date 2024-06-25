package com.saiful.shared.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    @field:Json(name = "file_path")
    val filePath: String = "",
    @field:Json(name = "vote_average")
    val voteAverage: Double = 0.0
) : Parcelable
