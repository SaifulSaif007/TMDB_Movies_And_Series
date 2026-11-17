package com.saiful.shared.data.repository

import com.saiful.base.network.model.GenericResponse
import com.saiful.shared.data.api.DiscoverApiService
import com.saiful.shared.model.discover.DiscoverFilter
import com.saiful.shared.model.discover.DiscoverMoviesResponse
import com.saiful.shared.model.discover.DiscoverSortBy
import com.saiful.shared.model.discover.DiscoverTvShowsResponse
import com.saiful.shared.model.discover.GenresResponse
import com.saiful.shared.model.discover.WatchProvidersListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor(
    private val discoverApiService: DiscoverApiService
) {

    suspend fun discoverMovies(
        page: Int = 1,
        filter: DiscoverFilter
    ): GenericResponse<DiscoverMoviesResponse> {
        val voteCountGte = if (filter.sortBy == DiscoverSortBy.VOTE_AVERAGE_DESC) 100 else null
        return discoverApiService.discoverMovies(
            page = page,
            sortBy = filter.sortBy.apiValue,
            withGenres = filter.genre?.id?.toString(),
            primaryReleaseYear = filter.year,
            withWatchProviders = filter.provider?.providerId?.toString(),
            voteCountGte = voteCountGte
        )
    }

    suspend fun discoverTvShows(
        page: Int = 1,
        filter: DiscoverFilter
    ): GenericResponse<DiscoverTvShowsResponse> {
        val voteCountGte = if (filter.sortBy == DiscoverSortBy.VOTE_AVERAGE_DESC) 50 else null
        return discoverApiService.discoverTvShows(
            page = page,
            sortBy = filter.sortBy.apiValue,
            withGenres = filter.genre?.id?.toString(),
            firstAirDateYear = filter.year,
            withWatchProviders = filter.provider?.providerId?.toString(),
            voteCountGte = voteCountGte
        )
    }

    suspend fun getMovieGenres(): GenericResponse<GenresResponse> =
        discoverApiService.getMovieGenres()

    suspend fun getTvGenres(): GenericResponse<GenresResponse> =
        discoverApiService.getTvGenres()

    suspend fun getMovieWatchProviders(watchRegion: String = "US"): GenericResponse<WatchProvidersListResponse> =
        discoverApiService.getMovieWatchProviders(watchRegion)

    suspend fun getTvWatchProviders(watchRegion: String = "US"): GenericResponse<WatchProvidersListResponse> =
        discoverApiService.getTvWatchProviders(watchRegion)
}
