package com.saiful.tmdbexplorer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
import com.saiful.movie.view.collection.MovieCollectionScreen
import com.saiful.movie.view.dashboard.MovieDashboardScreen
import com.saiful.movie.view.details.MovieDetailsScreen
import com.saiful.movie.view.list.MovieListScreen
import com.saiful.person.view.dashboard.PersonDashboardScreen
import com.saiful.person.view.details.PersonDetailsScreen
import com.saiful.person.view.list.PersonListScreen
import com.saiful.shared.model.Image
import com.saiful.shared.model.MovieCategory
import com.saiful.shared.model.TvShowsCategory
import com.saiful.shared.navigation.Route
import com.saiful.shared.utils.JsonConverter
import com.saiful.shared.view.gallery.GalleryScreen
import com.saiful.tmdbexplorer.search.SearchScreen
import com.saiful.tvshows.view.dashboard.TvShowsDashboardScreen
import com.saiful.tvshows.view.details.TvShowsDetailsScreen
import com.saiful.tvshows.view.list.TvShowListScreen
import com.saiful.tvshows.view.season.TvShowSeasonScreen
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
    TopLevelDestination("Celebrities", Icons.Default.Person, Route.PersonDashboard),
    TopLevelDestination("Search", Icons.Default.Search, Route.Search)
)

@OptIn(ExperimentalMaterial3Api::class)
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
                NavigationBar(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ) {
                    topLevelDestinations.forEach { destination ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.hasRoute(destination.route::class) } == true
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
                            icon = {
                                Icon(
                                    destination.icon,
                                    contentDescription = destination.name
                                )
                            },
                            label = { Text(destination.name) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color.Black,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }

        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.MovieDashboard,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
        ) {
            composable<Route.MovieDashboard> {
                MovieDashboardScreen(
                    viewModel = hiltViewModel(),
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) },
                    onSeeAllClick = { category -> navController.navigate(Route.MovieList(category.name)) }
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
                    onTrailerClick = { key -> launchYoutube(context, key) },
                    onCollectionClick = { collectionId ->
                        navController.navigate(
                            Route.MovieCollectionDetails(
                                collectionId
                            )
                        )
                    }
                )
            }

            composable<Route.MovieCollectionDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.MovieCollectionDetails>()
                MovieCollectionScreen(
                    collectionId = args.collectionId,
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) }
                )
            }

            composable<Route.MovieList> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.MovieList>()
                MovieListScreen(
                    category = MovieCategory.valueOf(args.category),
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) }
                )
            }

            composable<Route.TvShowsDashboard> {
                TvShowsDashboardScreen(
                    viewModel = hiltViewModel(),
                    onShowClick = { showId -> navController.navigate(Route.TvShowsDetails(showId)) },
                    onSeeAllClick = { category -> navController.navigate(Route.TvShowsList(category.name)) }
                )
            }

            composable<Route.TvShowsList> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.TvShowsList>()
                TvShowListScreen(
                    category = TvShowsCategory.valueOf(args.category),
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onShowClick = { showId -> navController.navigate(Route.TvShowsDetails(showId)) }
                )
            }

            composable<Route.Search> {
                SearchScreen(
                    onMovieClick = { movieId -> navController.navigate(Route.MovieDetails(movieId)) },
                    onShowClick = { showId -> navController.navigate(Route.TvShowsDetails(showId)) },
                    onPersonClick = { personId ->
                        navController.navigate(
                            Route.PersonDetails(
                                personId
                            )
                        )
                    }
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
                    onTrailerClick = { key -> launchYoutube(context, key) },
                    onSeasonClick = { showId, seasonNo ->
                        navController.navigate(
                            Route.TvShowSeasonDetails(
                                showId,
                                seasonNo
                            )
                        )
                    }
                )
            }

            composable<Route.TvShowSeasonDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.TvShowSeasonDetails>()
                TvShowSeasonScreen(
                    showId = args.showId,
                    seasonNo = args.seasonNo,
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable<Route.PersonDashboard> {
                PersonDashboardScreen(
                    viewModel = hiltViewModel(),
                    onPersonClick = { personId ->
                        navController.navigate(
                            Route.PersonDetails(
                                personId
                            )
                        )
                    },
                    onSeeAllClick = { category -> navController.navigate(Route.PersonList(category.name)) }
                )
            }

            composable<Route.PersonList> { backStackEntry ->
                val args = backStackEntry.toRoute<Route.PersonList>()
                PersonListScreen(
                    category = com.saiful.shared.model.PersonCategory.valueOf(args.category),
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.navigateUp() },
                    onPersonClick = { personId ->
                        navController.navigate(
                            Route.PersonDetails(
                                personId
                            )
                        )
                    }
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

@Composable
fun EdgeToEdgeBottomBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.navigationBars,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 3.dp // optional, matches BottomAppBar-ish look
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.error)
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets) // pushes content above the gesture/nav bar // your normal visual padding
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("OK")
            }
        }
    }
}