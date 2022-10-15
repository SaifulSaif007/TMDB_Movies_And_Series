package com.saiful.tvshows.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.saiful.base.util.navigation.TvShowModuleNavigation
import com.saiful.tvshows.R

class ShowNavigationImpl : TvShowModuleNavigation {

    override fun navigateToShowDetails(showId: Int, navController: NavController) {
        navController.navigate(R.id.tvshows_details_nav_graph, bundleOf("show_id" to showId))
    }
}