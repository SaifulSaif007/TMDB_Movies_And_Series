package com.saiful.tmdbexplorer.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TextMuted
import com.saiful.movie.view.components.MovieListItem
import com.saiful.shared.components.TMDBErrorView
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.account.AccountMediaTab
import com.saiful.tvshows.view.component.TvShowListItem

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val movies = viewModel.favoriteMovies.collectAsLazyPagingItems()
    val shows = viewModel.favoriteTvShows.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- Sticky Top Cinematic App Bar ---
        FavoritesTopBar(
            title = "My Favorites",
            onBackClick = onBackClick
        )

        // --- Media Tabs (Movies / TV Shows) ---
        FavoritesTabRow(
            selectedTab = selectedTab,
            onTabSelected = { viewModel.selectTab(it) }
        )

        // --- Screen Body Content ---
        if (!isLoggedIn) {
            FavoritesLoggedOutState(onNavigateToProfile = onNavigateToProfile)
        } else {
            when (selectedTab) {
                AccountMediaTab.MOVIES -> {
                    FavoriteMoviesContent(
                        movies = movies,
                        onMovieClick = onMovieClick
                    )
                }
                AccountMediaTab.TV_SHOWS -> {
                    FavoriteShowsContent(
                        shows = shows,
                        onShowClick = onShowClick
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritesTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowestDark.copy(alpha = 0.95f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHighDark)
                    .border(1.dp, GlassBorder, CircleShape)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = OnSurfaceDark,
                    modifier = Modifier.size(20.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        letterSpacing = (-0.3).sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CinemaCrimson)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(GlassBorder)
        )
    }
}

@Composable
private fun FavoritesTabRow(
    selectedTab: AccountMediaTab,
    onTabSelected: (AccountMediaTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = SurfaceContainerLowestDark,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = CinemaCrimson,
                height = 3.dp
            )
        },
        divider = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(GlassBorder)
            )
        }
    ) {
        AccountMediaTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        color = if (isSelected) CinemaCrimson else TextMuted
                    )
                }
            )
        }
    }
}

@Composable
private fun FavoriteMoviesContent(
    movies: LazyPagingItems<Movies>,
    onMovieClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        when (val state = movies.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView(modifier = Modifier.fillMaxSize())
            }
            is LoadState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TMDBErrorView(message = state.error.message ?: "Failed to load favorite movies")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { movies.retry() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Retry", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            is LoadState.NotLoading -> {
                if (movies.itemCount == 0) {
                    EmptyFavoritesState(mediaType = "movies")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            count = movies.itemCount,
                            key = movies.itemKey { it.id },
                            contentType = movies.itemContentType { "movie" }
                        ) { index ->
                            movies[index]?.let { movie ->
                                MovieListItem(movie = movie, onClick = onMovieClick)
                            }
                        }

                        when (val appendState = movies.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = CinemaCrimson,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = appendState.error.message ?: "Error loading more",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Button(
                                            onClick = { movies.retry() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = CinemaCrimson,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(text = "Retry", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteShowsContent(
    shows: LazyPagingItems<TvShows>,
    onShowClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        when (val state = shows.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView(modifier = Modifier.fillMaxSize())
            }
            is LoadState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TMDBErrorView(message = state.error.message ?: "Failed to load favorite TV shows")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { shows.retry() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Retry", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            is LoadState.NotLoading -> {
                if (shows.itemCount == 0) {
                    EmptyFavoritesState(mediaType = "TV shows")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            count = shows.itemCount,
                            key = shows.itemKey { it.id },
                            contentType = shows.itemContentType { "tvshow" }
                        ) { index ->
                            shows[index]?.let { show ->
                                TvShowListItem(show = show, onClick = onShowClick)
                            }
                        }

                        when (val appendState = shows.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = CinemaCrimson,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = appendState.error.message ?: "Error loading more",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Button(
                                            onClick = { shows.retry() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = CinemaCrimson,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(text = "Retry", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFavoritesState(mediaType: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(SurfaceContainerDark)
                .border(1.dp, GlassBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = CinemaCrimson,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Favorite ${mediaType.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark,
                fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explore $mediaType and tap the heart icon on any title to save it to your favorites.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                fontSize = 13.sp,
                lineHeight = 19.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FavoritesLoggedOutState(onNavigateToProfile: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(SurfaceContainerDark)
                .border(1.dp, GlassBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = CinemaCrimson,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sign In for Favorites",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark,
                fontSize = 20.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Connect your TMDB account to save, sync, and access your favorite movies and TV shows anywhere.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                fontSize = 13.sp,
                lineHeight = 20.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToProfile,
            colors = ButtonDefaults.buttonColors(
                containerColor = CinemaCrimson,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(text = "Go to Profile", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}
