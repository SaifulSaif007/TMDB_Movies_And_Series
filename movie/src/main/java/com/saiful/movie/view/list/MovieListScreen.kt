package com.saiful.movie.view.list

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.movie.view.components.MovieListItem
import com.saiful.shared.components.TMDBErrorView
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.model.MovieCategory
import com.saiful.shared.model.Movies
import kotlinx.coroutines.flow.flowOf

@Composable
fun MovieListScreen(
    category: MovieCategory,
    viewModel: ListVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val movies = viewModel.movieList.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- Sticky Cinematic Top App Bar ---
        MovieListTopBar(
            title = category.value,
            onBackClick = onBackClick
        )

        // --- Screen Body Content with Paging & Load States ---
        MovieListContent(
            movies = movies,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun MovieListTopBar(
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
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Circular Back Navigation Button
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

            // Screen Title with Pulsing Crimson Dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp
                    )
                )

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CinemaCrimson)
                )
            }
        }

        // Hairline bottom divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(GlassBorder)
        )
    }
}

@Composable
fun MovieListContent(
    movies: LazyPagingItems<Movies>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
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

            // Append state handling (pagination loading / error)
            when (val state = movies.loadState.append) {
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
                                modifier = Modifier.size(32.dp)
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
                                text = state.error.message ?: "Error loading more movies",
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

        // Refresh state handling (initial load / error)
        when (val state = movies.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView(modifier = Modifier.fillMaxSize())
            }

            is LoadState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TMDBErrorView(message = state.error.message ?: "Error loading movies")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { movies.retry() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Retry", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            else -> {}
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun MovieListContentPreview() {
    val movies = listOf(
        Movies(
            id = 1,
            title = "Spider-Man: Brand New Day",
            overview = "Fighting crime full-time as Spider-Man in a world that doesn't remember him...",
            voteAverage = 8.5,
            releaseDate = "2026-05-01",
            posterPath = null
        ),
        Movies(
            id = 2,
            title = "Project Hail Mary",
            overview = "Science teacher Ryland Grace wakes up on a spaceship light years from home...",
            voteAverage = 8.6,
            releaseDate = "2026-03-20",
            posterPath = null
        ),
        Movies(
            id = 3,
            title = "Avengers: Doomsday",
            overview = "Beloved heroes from three distinct universes are set on a deadly collision...",
            voteAverage = 9.1,
            releaseDate = "2026-05-01",
            posterPath = null
        )
    )
    val pagingData = PagingData.from(movies)
    val fakeDataFlow = flowOf(pagingData)
    val lazyPagingItems = fakeDataFlow.collectAsLazyPagingItems()

    TMDBTheme {
        MovieListContent(
            movies = lazyPagingItems,
            onMovieClick = {}
        )
    }
}
