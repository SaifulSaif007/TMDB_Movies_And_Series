package com.saiful.tmdb2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.saiful.tmdb2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    private var currentDestination: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                com.saiful.movie.R.id.movie_nav_graph,
                com.saiful.tvshows.R.id.tvshows_nav_graph,
                com.saiful.person.R.id.person_nav_graph,
                com.saiful.shared.R.id.galleryFragment,
                R.id.searchFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navBar = findViewById<BottomNavigationView>(R.id.nav_bar_container)
        navBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestination = destination.id

            when (currentDestination) {
                com.saiful.movie.R.id.movieDetailsFragment,
                com.saiful.movie.R.id.collectionFragment,
                com.saiful.tvshows.R.id.tvShowsDetailsFragment,
                com.saiful.tvshows.R.id.showSeasonFragment,
                com.saiful.shared.R.id.galleryFragment,
                R.id.searchFragment -> {
                    toggleAppBarLayoutVisibility(false)
                }

                else -> {
                    toggleAppBarLayoutVisibility(true)
                }
            }

            when (currentDestination) {
                com.saiful.movie.R.id.movieDashboardFragment,
                com.saiful.tvshows.R.id.tvShowsFragment,
                com.saiful.person.R.id.personDashboardFragment,
                R.id.searchFragment -> {
                    toggleBottomBarVisibility(true)
                }

                else -> toggleBottomBarVisibility(false)

            }
        }

    }

    private fun toggleAppBarLayoutVisibility(visible: Boolean) {
        binding.appBarLayout.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun toggleBottomBarVisibility(visible: Boolean) {
        binding.navBarContainer.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}