package com.saiful.person.data.repository

import com.saiful.person.data.api.PersonApiService
import javax.inject.Inject

class DashboardRepo
@Inject constructor(private val apiService: PersonApiService) {

    suspend fun popularPersons() = apiService.popularPersons()

    suspend fun trendingPersons() = apiService.trendingPersons()
}