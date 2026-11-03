package com.saiful.tvshows.view.dashboard

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
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBImageSlider
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.components.TMDBTvShowItem
import com.saiful.shared.model.SliderItem
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.TvShowsCategory

@Composable
fun TvShowsDashboardScreen(
    viewModel: ShowsDashboardVM,
    onShowClick: (Int) -> Unit,
    onSeeAllClick: (TvShowsCategory) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    TvShowsDashboardContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onShowClick = onShowClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
fun TvShowsDashboardContent(
    modifier: Modifier = Modifier,
    uiState: DashboardUiState,
    onShowClick: (Int) -> Unit,
    onSeeAllClick: (TvShowsCategory) -> Unit
) {
    val isLoading = uiState.trendingShows.isEmpty() &&
            uiState.popularShows.isEmpty() &&
            uiState.topRatedShows.isEmpty() &&
            uiState.onAirShows.isEmpty()

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
                if (uiState.sliderShows.isNotEmpty()) {
                    TMDBImageSlider(
                        items = uiState.sliderShows.map {
                            SliderItem(
                                id = it.id,
                                title = it.name,
                                backdropPath = it.backdropPath,
                                rating = it.voteAverage,
                                releaseDate = it.firstAirDate,
                                overview = it.overview
                            )
                        },
                        onItemClick = onShowClick,
                        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
                    )
                }

                // Section: Trending TV Shows
                TvShowSection(
                    title = "Trending TV Shows",
                    shows = uiState.trendingShows,
                    onShowClick = onShowClick,
                    onSeeAllClick = { onSeeAllClick(TvShowsCategory.TRENDING) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Section: Popular TV Shows
                TvShowSection(
                    title = "Popular TV Shows",
                    shows = uiState.popularShows,
                    onShowClick = onShowClick,
                    onSeeAllClick = { onSeeAllClick(TvShowsCategory.POPULAR) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Section: Top Rated Shows
                TvShowSection(
                    title = "Top Rated Shows",
                    shows = uiState.topRatedShows,
                    onShowClick = onShowClick,
                    onSeeAllClick = { onSeeAllClick(TvShowsCategory.TOP_RATED) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Section: On Air Today
                TvShowSection(
                    title = "On Air Today",
                    shows = uiState.onAirShows,
                    onShowClick = onShowClick,
                    onSeeAllClick = { onSeeAllClick(TvShowsCategory.ON_AIR) }
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Cinema Projector / Clapper Icon Emblem
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(CinemaCrimson, Color(0xFF8B0000))
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
private fun TvShowSection(
    title: String,
    shows: List<TvShows>,
    onShowClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    if (shows.isEmpty()) return

    Column {
        TMDBSectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick
        )
        TMDBHorizontalList(
            items = shows,
            itemContent = { show ->
                TMDBTvShowItem(
                    tvShow = show,
                    onClick = onShowClick
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
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Carousel Banner Skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerHighDark.copy(alpha = alpha))
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
        )

        // Section Title Skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(SurfaceContainerHighDark.copy(alpha = alpha))
            )
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(SurfaceContainerHighDark.copy(alpha = alpha))
            )
        }

        // Horizontal Row Skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) {
                Column(
                    modifier = Modifier.width(135.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(195.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceContainerHighDark.copy(alpha = alpha))
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceContainerHighDark.copy(alpha = alpha))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceContainerHighDark.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun TvShowsDashboardContentPreview() {
    val dummyShow1 = TvShows(
        id = 1,
        name = "Law & Order: Special Victims Unit",
        posterPath = null,
        backdropPath = null,
        voteAverage = 8.8,
        firstAirDate = "1999-09-20",
        overview = "In the criminal justice system, sexually-based offenses are considered especially heinous."
    )
    val dummyShow2 = TvShows(
        id = 2,
        name = "Lupin",
        posterPath = null,
        backdropPath = null,
        voteAverage = 7.5,
        firstAirDate = "2021-01-08",
        overview = "Inspired by the adventures of Arsène Lupin, gentleman thief Assane Diop sets out to avenge his father."
    )
    val dummyShow3 = TvShows(
        id = 3,
        name = "Link Click",
        posterPath = null,
        backdropPath = null,
        voteAverage = 8.7,
        firstAirDate = "2021-04-30",
        overview = "Using superpowers to enter their clientele's photos one by one, Cheng Xiaoshi and Lu Guang take their work seriously."
    )

    TMDBTheme {
        TvShowsDashboardContent(
            uiState = DashboardUiState(
                trendingShows = listOf(dummyShow1, dummyShow2, dummyShow3),
                popularShows = listOf(dummyShow2, dummyShow3, dummyShow1),
                topRatedShows = listOf(dummyShow3, dummyShow1, dummyShow2),
                onAirShows = listOf(dummyShow1, dummyShow2),
                sliderShows = listOf(dummyShow1, dummyShow2)
            ),
            onShowClick = {},
            onSeeAllClick = {}
        )
    }
}
