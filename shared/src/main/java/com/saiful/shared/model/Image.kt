package com.saiful.shared.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Image(
    @SerialName("file_path")
    val filePath: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0
) : Parcelable

