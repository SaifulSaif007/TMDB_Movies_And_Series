package com.saiful.person.data.repository

import com.saiful.person.data.api.PersonApiService
import javax.inject.Inject

class DashboardRepo
@Inject constructor(private val apiService: PersonApiService) {

    suspend fun popularPersons() = apiService.popularPersons(1)

    suspend fun trendingPersons() = apiService.trendingPersons(1)
}