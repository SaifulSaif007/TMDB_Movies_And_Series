package com.saiful.tvshows.data.repository

import com.saiful.tvshows.data.api.TvShowsApiService
import javax.inject.Inject

class SeasonRepo
@Inject constructor(private val apiService: TvShowsApiService) {

    suspend fun seasonDetails(showId: Int, seasonNo: Int) =
        apiService.seasonDetails(showId, seasonNo)
}