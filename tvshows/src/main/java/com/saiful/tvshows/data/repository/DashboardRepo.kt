package com.saiful.tvshows.data.repository

import com.saiful.tvshows.data.api.TvShowsApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepo
@Inject constructor(private val tvShowsApiService: TvShowsApiService) {

    suspend fun getTrendingShows(pageNo: Int) = tvShowsApiService.trendingTvShows(pageNo)

    suspend fun getPopularShows(pageNo: Int) = tvShowsApiService.popularTvShows(pageNo)

    suspend fun getTopRatedShows(pageNo: Int) = tvShowsApiService.topRatedTvShows(pageNo)

    suspend fun getOnAirShows(pageNo: Int) = tvShowsApiService.onAirTvShows(pageNo)
}