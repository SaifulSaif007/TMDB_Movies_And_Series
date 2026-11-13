package com.saiful.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchProvidersResponse(
    val id: Int? = null,
    val results: Map<String, CountryWatchProviders>? = null
)

@Serializable
data class CountryWatchProviders(
    val link: String? = null,
    val flatrate: List<WatchProviderItem>? = null,
    val rent: List<WatchProviderItem>? = null,
    val buy: List<WatchProviderItem>? = null,
    val free: List<WatchProviderItem>? = null,
    val ads: List<WatchProviderItem>? = null
) {
    val hasProviders: Boolean
        get() = !flatrate.isNullOrEmpty() ||
                !rent.isNullOrEmpty() ||
                !buy.isNullOrEmpty() ||
                !free.isNullOrEmpty() ||
                !ads.isNullOrEmpty()
}

@Serializable
data class WatchProviderItem(
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("provider_id")
    val providerId: Int? = null,
    @SerialName("provider_name")
    val providerName: String? = null,
    @SerialName("display_priority")
    val displayPriority: Int? = null
)
