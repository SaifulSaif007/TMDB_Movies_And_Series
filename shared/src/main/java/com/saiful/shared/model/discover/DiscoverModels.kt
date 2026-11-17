package com.saiful.shared.model.discover

import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.WatchProviderItem
import com.saiful.shared.model.account.AccountMediaTab
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverMoviesResponse(
    val page: Int = 1,
    val results: List<Movies> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
    @SerialName("total_results") val totalResults: Int = 0
)

@Serializable
data class DiscoverTvShowsResponse(
    val page: Int = 1,
    val results: List<TvShows> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
    @SerialName("total_results") val totalResults: Int = 0
)

@Serializable
data class GenresResponse(
    val genres: List<GenreItem> = emptyList()
)

@Serializable
data class GenreItem(
    val id: Int,
    val name: String
)

@Serializable
data class WatchProvidersListResponse(
    val results: List<WatchProviderItem> = emptyList()
)

enum class DiscoverSortBy(val apiValue: String, val title: String) {
    POPULARITY_DESC("popularity.desc", "Most Popular"),
    VOTE_AVERAGE_DESC("vote_average.desc", "Highest Rated"),
    PRIMARY_RELEASE_DATE_DESC("primary_release_date.desc", "Newest"),
    REVENUE_DESC("revenue.desc", "Box Office")
}

data class DiscoverFilter(
    val mediaTab: AccountMediaTab = AccountMediaTab.MOVIES,
    val genre: GenreItem? = null,
    val year: Int? = null,
    val provider: WatchProviderItem? = null,
    val sortBy: DiscoverSortBy = DiscoverSortBy.POPULARITY_DESC
) {
    val activeFilterCount: Int
        get() {
            var count = 0
            if (genre != null) count++
            if (year != null) count++
            if (provider != null) count++
            if (sortBy != DiscoverSortBy.POPULARITY_DESC) count++
            return count
        }

    val isDefault: Boolean
        get() = activeFilterCount == 0
}
