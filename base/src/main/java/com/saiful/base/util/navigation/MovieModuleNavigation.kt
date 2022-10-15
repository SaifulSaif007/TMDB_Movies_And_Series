package com.saiful.base.util.navigation

import androidx.navigation.NavController

interface MovieModuleNavigation {

    fun navigateMovieDetails(movieId: Int, navController: NavController)

}