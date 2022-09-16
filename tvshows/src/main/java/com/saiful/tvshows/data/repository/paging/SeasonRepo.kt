package com.saiful.tvshows.data.repository.paging

import com.saiful.tvshows.data.api.TvShowsApiService
import javax.inject.Inject

class SeasonRepo
@Inject constructor(private val apiService: TvShowsApiService) {

    suspend fun seasonDetails(id: Int, no: Int) = apiService.seasonDetails(id, no)
}