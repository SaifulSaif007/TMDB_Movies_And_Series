package com.saiful.movie.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.saiful.base.util.navigation.MovieModuleNavigation
import com.saiful.movie.R

class MovieNavigationImpl : MovieModuleNavigation {

    override fun navigateMovieDetails(movieId: Int, navController: NavController) {
        navController.navigate(R.id.movie_details_nav_graph, bundleOf("movie_id" to movieId))
    }

}