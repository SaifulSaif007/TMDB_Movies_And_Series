package com.saiful.tmdbexplorer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.movie.view.dashboard.MovieDashboardScreen
import com.saiful.movie.view.details.MovieDetailsScreen
import com.saiful.movie.view.list.MovieListScreen
import com.saiful.person.view.dashboard.PersonDashboardScreen
import com.saiful.person.view.details.PersonDetailsScreen
import com.saiful.shared.model.Image
import com.saiful.shared.navigation.Route
import com.saiful.shared.utils.JsonConverter
import com.saiful.shared.view.gallery.GalleryScreen
import com.saiful.tvshows.view.dashboard.TvShowsDashboardScreen
import com.saiful.tvshows.view.details.TvShowsDetailsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TMDBTheme {
                MainScreen(context = this)
            }
        }
    }
}

data class TopLevelDestination<T : Any>(
    val name: String,
    val icon: ImageVector,
    val route: T
)

val topLevelDestinations = listOf(
    TopLevelDestination("Movies", Icons.Default.Home, Route.MovieDashboard),
    TopLevelDestination("TV Shows", Icons.Default.PlayArrow, Route.TvShowsDashboard),
    TopLevelDestination("Celebrities", Icons.Default.Person, Route.PersonDashboard)
)

@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = topLevelDestinations.any { destination ->
        currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topLevelDestinations.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(destination.icon, contentDescription = destination.name) },
                            label = { Text(destination.name) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.MovieDashboard,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable<Route.MovieDashboard> {
                MovieDashboardScreen(
                    viewModel = hiltViewModel(),
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) },
                    onSeeAllClick = { category -> navController.navigate(Route.MovieList(category)) }
                )
            }

            composable<Route.MovieDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.MovieDetails>()
                MovieDetailsScreen(
                    movieId = args.movieId,
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) },
                    onCastClick = { castId -> navController.navigate(Route.PersonDetails(castId)) },
                    onTrailerClick = { key -> launchYoutube(context, key) }
                )
            }

            composable<Route.MovieList> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.MovieList>()
                MovieListScreen(
                    category = args.category,
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) }
                )
            }

            composable<Route.TvShowsDashboard> {
                TvShowsDashboardScreen(
                    viewModel = hiltViewModel(),
                    onShowClick = { showId -> navController.navigate(Route.TvShowsDetails(showId)) },
                    onSeeAllClick = { /* Not fully implemented yet */ }
                )
            }

            composable<Route.TvShowsDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.TvShowsDetails>()
                TvShowsDetailsScreen(
                    showId = args.showId,
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onShowClick = { showId -> navController.navigate(Route.TvShowsDetails(showId)) },
                    onCastClick = { castId -> navController.navigate(Route.PersonDetails(castId)) },
                    onTrailerClick = { key -> launchYoutube(context, key) }
                )
            }

            composable<Route.PersonDashboard> {
                PersonDashboardScreen(
                    viewModel = hiltViewModel(),
                    onPersonClick = { personId -> navController.navigate(Route.PersonDetails(personId)) },
                    onSeeAllClick = { /* Not fully implemented yet */ }
                )
            }

            composable<Route.PersonDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.PersonDetails>()
                PersonDetailsScreen(
                    personId = args.personId,
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) },
                    onShowClick = { showId -> navController.navigate(Route.TvShowsDetails(showId)) },
                    onImageClick = { position, images ->
                        val imagesJson = JsonConverter.toJsonList(images)
                        navController.navigate(Route.Gallery(imagesJson, position))
                    }
                )
            }

            composable<Route.Gallery> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.Gallery>()
                val images = JsonConverter.fromJsonList<Image>(args.imagesJson) ?: emptyList()
                GalleryScreen(
                    images = images,
                    startIndex = args.startIndex,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}

private fun launchYoutube(context: Context, key: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$key"))
    context.startActivity(intent)
}
