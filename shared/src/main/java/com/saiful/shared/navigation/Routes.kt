package com.saiful.shared.navigation

import com.saiful.shared.model.MovieCategory
import com.saiful.shared.model.PersonCategory
import com.saiful.shared.model.TvShowsCategory
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object MovieDashboard : Route

    @Serializable
    data class MovieDetails(val movieId: Int) : Route

    @Serializable
    data class MovieList(val category: MovieCategory) : Route

    @Serializable
    data class MovieCollectionDetails(val collectionId: Int) : Route

    @Serializable
    data object TvShowsDashboard : Route

    @Serializable
    data class TvShowsDetails(val showId: Int) : Route

    @Serializable
    data class TvShowsList(val category: TvShowsCategory) : Route
    
    @Serializable
    data class TvShowSeasonDetails(val showId: Int, val seasonNo: Int) : Route

    @Serializable
    data object PersonDashboard : Route

    @Serializable
    data class PersonDetails(val personId: Int) : Route

    @Serializable
    data class PersonList(val category: PersonCategory) : Route

    @Serializable
    data object Search : Route

    @Serializable
    data class Gallery(val imagesJson: String, val startIndex: Int) : Route
}
