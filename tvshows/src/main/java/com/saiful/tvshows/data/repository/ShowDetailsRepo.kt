package com.saiful.tvshows.data.repository

import com.saiful.tvshows.data.api.TvShowsApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowDetailsRepo
@Inject constructor(private val apiService: TvShowsApiService) {

    suspend fun showDetails(id: Int) = apiService.showDetails(id)

    suspend fun showCasts(id: Int) = apiService.showCasts(id)

    suspend fun showRecommendation(id: Int) = apiService.recommendations(id)
}