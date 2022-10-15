package com.saiful.person.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.saiful.base.util.navigation.PersonModuleNavigation
import com.saiful.person.R

class NavigationImpl : PersonModuleNavigation {

    override fun navigateToPersonDetails(personId: Int, navController: NavController) {

        navController.navigate(R.id.person_details_nav_graph, bundleOf("person_id" to personId))

    }
}