package com.saiful.movie.view.dashboard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.CrimsonBright
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBImageSlider
import com.saiful.shared.components.TMDBMovieItem
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.model.MovieCategory
import com.saiful.shared.model.Movies
import com.saiful.shared.model.SliderItem

@Composable
fun MovieDashboardScreen(
    viewModel: DashboardVM,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (MovieCategory) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    MovieDashboardContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onMovieClick = onMovieClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
fun MovieDashboardContent(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (MovieCategory) -> Unit
) {
    val isLoading = uiState.popularMovies.isEmpty() &&
            uiState.nowPlayingMovies.isEmpty() &&
            uiState.topRatedMovies.isEmpty() &&
            uiState.upcomingMovies.isEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- 1. Top Cinematic Header Bar ---
        TopCinematicHeader()

        // --- 2. Scrollable Main Content ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            if (isLoading) {
                // Skeleton loading state
                DashboardSkeleton()
            } else {
                // Auto-sliding Hero Spotlight Carousel
                if (uiState.sliderMovies.isNotEmpty()) {
                    TMDBImageSlider(
                        items = uiState.sliderMovies.map {
                            SliderItem(
                                id = it.id,
                                title = it.title,
                                backdropPath = it.backdropPath,
                                rating = it.voteAverage,
                                releaseDate = it.releaseDate,
                                overview = it.overview
                            )
                        },
                        onItemClick = onMovieClick,
                        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                    )
                }

                // Section: Popular Movies
                MovieSection(
                    title = "Popular Movies",
                    movies = uiState.popularMovies,
                    onMovieClick = onMovieClick,
                    onSeeAllClick = { onSeeAllClick(MovieCategory.POPULAR) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Section: Now Playing In Theatres
                MovieSection(
                    title = "Now Playing In Theatres",
                    movies = uiState.nowPlayingMovies,
                    onMovieClick = onMovieClick,
                    onSeeAllClick = { onSeeAllClick(MovieCategory.NOW_PLAYING) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Section: Top Rated Classics
                MovieSection(
                    title = "Top Rated Classics",
                    movies = uiState.topRatedMovies,
                    onMovieClick = onMovieClick,
                    onSeeAllClick = { onSeeAllClick(MovieCategory.TOP_RATED) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Section: Upcoming Releases
                MovieSection(
                    title = "Upcoming Releases",
                    movies = uiState.upcomingMovies,
                    onMovieClick = onMovieClick,
                    onSeeAllClick = { onSeeAllClick(MovieCategory.UPCOMING) }
                )
            }
        }
    }
}

@Composable
private fun TopCinematicHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowestDark.copy(alpha = 0.95f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo & Brand Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Clapperboard Monogram Emblem
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(CinemaCrimson, CrimsonBright)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        val w = size.width
                        val h = size.height
                        drawRoundRect(
                            color = Color.White,
                            size = size,
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                        drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = Offset(w * 0.32f, h * 0.5f))
                        drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = Offset(w * 0.68f, h * 0.5f))
                    }
                }

                // Wordmark
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cine",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "Wave",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CinemaCrimson,
                            fontSize = 20.sp,
                            letterSpacing = (-0.5).sp
                        )
                    )
                }
            }

            // Action Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Notification Bell with Active Indicator Dot
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighDark)
                        .border(1.dp, GlassBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = OnSurfaceDark.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                    // Red indicator dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 7.dp, end = 7.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CinemaCrimson)
                    )
                }

                // Profile Avatar Chip
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(SurfaceContainerHighDark, SurfaceContainerDark)
                            )
                        )
                        .border(1.dp, GlassBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CW",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CinemaCrimson,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
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
private fun MovieSection(
    title: String,
    movies: List<Movies>,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    if (movies.isEmpty()) return

    Column {
        TMDBSectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick
        )
        TMDBHorizontalList(
            items = movies,
            itemContent = { movie ->
                TMDBMovieItem(
                    movie = movie,
                    onClick = onMovieClick
                )
            }
        )
    }
}

@Composable
private fun DashboardSkeleton() {
    val infiniteTransition = rememberInfiniteTransition(label = "SkeletonTransition")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.60f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SkeletonAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceContainerDark.copy(alpha = alpha))
        )

        // Section 1 Header & Row
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceContainerDark.copy(alpha = alpha))
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(135.dp)
                        .height(195.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerDark.copy(alpha = alpha))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieSectionPreview() {
    val dummyMovie = Movies(
        id = 1,
        title = "Spider-Man: No Way Home",
        posterPath = "/1g0dhYEjmvl6Y7KEmUz9AUIXo7r.jpg",
        voteAverage = 8.2,
        releaseDate = "2021-12-15"
    )
    TMDBTheme {
        MovieDashboardContent(
            uiState = DashboardUiState(
                popularMovies = listOf(dummyMovie, dummyMovie),
                sliderMovies = listOf(dummyMovie)
            ),
            onMovieClick = {},
            onSeeAllClick = {}
        )
    }
}
