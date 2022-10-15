package com.saiful.base.util.navigation

import androidx.navigation.NavController

interface TvShowModuleNavigation {

    fun navigateToShowDetails(showId: Int, navController: NavController)
}