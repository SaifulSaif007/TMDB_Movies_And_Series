package com.saiful.base.util.navigation

import androidx.navigation.NavController

interface PersonModuleNavigation {

    fun navigateToPersonDetails(personId: Int, navController: NavController)

}