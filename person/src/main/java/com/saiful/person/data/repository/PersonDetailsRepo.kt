package com.saiful.person.data.repository

import com.saiful.person.data.api.PersonApiService
import javax.inject.Inject

class PersonDetailsRepo
@Inject constructor(private val apiService: PersonApiService) {

    suspend fun personDetails(personId: Int) = apiService.personDetails(personId)
    suspend fun personImages(personId: Int) = apiService.personImage(personId)
    suspend fun personMovieCredits(personId: Int) = apiService.movieCredits(personId)
    suspend fun personTvShowsCredits(personId: Int) = apiService.tvShowsCredits(personId)
}