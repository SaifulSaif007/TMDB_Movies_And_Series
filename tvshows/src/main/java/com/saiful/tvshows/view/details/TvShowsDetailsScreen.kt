package com.saiful.tvshows.view.details

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.CrimsonLight
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.shared.components.TMDBCastItem
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.components.TMDBTrailerItem
import com.saiful.shared.components.TMDBTvShowItem
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import com.saiful.tvshows.model.Genre
import com.saiful.tvshows.model.Season
import com.saiful.tvshows.model.TvShowDetails
import java.util.Locale

@Composable
fun TvShowsDetailsScreen(
    showId: Int,
    viewModel: TvShowsDetailsVM,
    onBackClick: () -> Unit,
    onShowClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onTrailerClick: (String) -> Unit,
    onSeasonClick: (Int, Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(showId) {
        viewModel.fetchShowDetails(showId)
    }

    TvShowsDetailsContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onBackClick = onBackClick,
        onShowClick = onShowClick,
        onCastClick = onCastClick,
        onTrailerClick = onTrailerClick,
        onSeasonClick = onSeasonClick
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TvShowsDetailsContent(
    uiState: TvShowsDetailsUiState,
    onBackClick: () -> Unit,
    onShowClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onTrailerClick: (String) -> Unit,
    onSeasonClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val show = uiState.showDetails
    val context = LocalContext.current
    var isOverviewExpanded by remember { mutableStateOf(false) }
    val trailers = show?.videos?.results?.filter { it.type == "Trailer" || it.type == "Teaser" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- 1. Sticky Top Cinematic App Bar ---
        TvShowsDetailsTopBar(
            title = show?.name ?: "",
            onBackClick = onBackClick,
            onShareClick = {
                shareTvShow(context, show?.name ?: "TV Series", show?.id ?: 0)
            }
        )

        if (show == null) {
            TMDBLoadingView(modifier = Modifier.fillMaxSize())
            return@Column
        }

        // --- 2. Scrollable Body Content ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Hero Backdrop Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
                    .background(SurfaceContainerLowestDark)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + show.backdropPath,
                    contentDescription = show.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Multi-layer gradient scrim: smooth fade into obsidian canvas
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    ObsidianCanvas.copy(alpha = 0.50f),
                                    ObsidianCanvas
                                ),
                                startY = 60f
                            )
                        )
                )

                // Top Format Badges (4K UHD & IMAX)
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerLowestDark.copy(alpha = 0.85f))
                            .border(0.5.dp, GlassBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "4K UHD",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonAmber.copy(alpha = 0.20f))
                            .border(1.dp, NeonAmber.copy(alpha = 0.40f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "IMAX",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            }

            // Inset Overlapping Floating Poster & Quick Actions Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val overlapPx = 50.dp.roundToPx()
                        layout(placeable.width, (placeable.height - overlapPx).coerceAtLeast(0)) {
                            placeable.placeRelative(0, -overlapPx)
                        }
                    },
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // TV Series Poster with Rating Overlay
                val posterShape = RoundedCornerShape(16.dp)
                val voteAverage = show.voteAverage
                Box(
                    modifier = Modifier
                        .width(115.dp)
                        .aspectRatio(2f / 3f)
                        .clip(posterShape)
                        .background(SurfaceContainerDark)
                        .border(2.dp, GlassBorder, posterShape)
                ) {
                    AsyncImage(
                        model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + show.posterPath,
                        contentDescription = show.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Poster Rating Pill
                    if (voteAverage != null && voteAverage > 0.0) {
                        Row(
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.TopStart)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.75f))
                                .border(1.dp, NeonAmber.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = NeonAmber,
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = String.format(Locale.US, "%.1f", voteAverage),
                                color = NeonAmber,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Quick Action Buttons Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Rating & Episode count badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (voteAverage != null && voteAverage > 0.0) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceContainerHighDark)
                                    .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = NeonAmber,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = String.format(Locale.US, "%.1f", voteAverage),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = "/ 10",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        val totalEpisodes = show.numberOfEpisodes
                        if (totalEpisodes != null && totalEpisodes > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceContainerDark)
                                    .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "$totalEpisodes Eps",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    // Watch Trailer CTA Button
                    val firstTrailerKey = trailers?.firstOrNull()?.key
                    Button(
                        onClick = {
                            if (!firstTrailerKey.isNullOrBlank()) {
                                onTrailerClick(firstTrailerKey)
                            }
                        },
                        enabled = !firstTrailerKey.isNullOrBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaCrimson,
                            contentColor = Color.White,
                            disabledContainerColor = SurfaceContainerHighDark,
                            disabledContentColor = TextMuted
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Watch Trailer",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }

                    // Share Series Button
                    Button(
                        onClick = {
                            shareTvShow(context, show.name, show.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHighDark,
                            contentColor = OnSurfaceDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = OnSurfaceDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share Series",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = OnSurfaceDark
                            )
                        )
                    }
                }
            }

            // Title & Network Badge & Genre Chips Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Network badge (e.g. NBC Original Series)
                val networkName = show.networks?.firstOrNull()?.name
                if (!networkName.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(CinemaCrimson)
                        )
                        Text(
                            text = "$networkName ORIGINAL SERIES",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CrimsonLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.8.sp
                            )
                        )
                    }
                }

                // Title
                Text(
                    text = show.name ?: "",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp,
                        letterSpacing = (-0.5).sp
                    )
                )

                // Genre Chips
                val genres = show.genres?.filterNotNull()
                if (!genres.isNullOrEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genres.forEach { genre ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(SurfaceContainerHighDark)
                                    .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = genre.name ?: "",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = OnSurfaceDark,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Tagline & Synopsis Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val tagline = show.tagline
                if (!tagline.isNullOrBlank()) {
                    val quoteShape = RoundedCornerShape(12.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(quoteShape)
                            .background(SurfaceContainerDark)
                            .border(1.dp, GlassBorder, quoteShape)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "“$tagline”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                color = NeonAmber.copy(alpha = 0.95f),
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }

                val overview = show.overview
                if (!overview.isNullOrBlank()) {
                    Column {
                        Text(
                            text = overview,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = OnSurfaceDark.copy(alpha = 0.88f),
                                fontSize = 13.sp,
                                lineHeight = 21.sp
                            ),
                            maxLines = if (isOverviewExpanded) Int.MAX_VALUE else 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (overview.length > 120) {
                            Row(
                                modifier = Modifier
                                    .clickable { isOverviewExpanded = !isOverviewExpanded }
                                    .padding(top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = if (isOverviewExpanded) "Show less" else "Read more",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = CinemaCrimson,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                                Icon(
                                    imageVector = if (isOverviewExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = CinemaCrimson,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Information Metadata Section
            TvShowInfoSection(show = show)

            // Horizontal Seasons Carousel
            val seasons = show.seasons?.filter { (it.episodeCount ?: 0) > 0 || !it.posterPath.isNullOrBlank() }
            if (!seasons.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                TMDBSectionHeader(
                    title = "Seasons",
                    onSeeAllClick = {},
                    showSeeAll = false
                )
                TMDBHorizontalList(
                    items = seasons,
                    itemContent = { season ->
                        SeasonCard(
                            season = season,
                            onClick = { onSeasonClick(show.id ?: 0, season.seasonNumber ?: 0) }
                        )
                    }
                )
            }

            // Cast Carousel
            val casts = uiState.showCasts?.cast
            if (!casts.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                TMDBSectionHeader(
                    title = "Cast",
                    onSeeAllClick = {},
                    showSeeAll = false
                )
                TMDBHorizontalList(
                    items = casts,
                    itemContent = { cast ->
                        TMDBCastItem(
                            name = cast.name ?: "",
                            character = cast.roles.firstOrNull()?.character ?: "",
                            profilePath = cast.profilePath,
                            onClick = { onCastClick(cast.id) }
                        )
                    }
                )
            }

            // Trailers Carousel
            if (!trailers.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                TMDBSectionHeader(
                    title = "Trailers",
                    onSeeAllClick = {},
                    showSeeAll = false
                )
                TMDBHorizontalList(
                    items = trailers,
                    itemContent = { trailer ->
                        TMDBTrailerItem(
                            name = trailer.name ?: "",
                            key = trailer.key ?: "",
                            onClick = onTrailerClick
                        )
                    }
                )
            }

            // Recommendations Carousel
            val recommendations = uiState.recommendations?.results
            if (!recommendations.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                TMDBSectionHeader(
                    title = "Recommendations",
                    onSeeAllClick = {},
                    showSeeAll = false
                )
                TMDBHorizontalList(
                    items = recommendations,
                    itemContent = { recommendation ->
                        TMDBTvShowItem(tvShow = recommendation, onClick = onShowClick)
                    }
                )
            }

            // Similar Shows Carousel
            val similarShows = uiState.similarShows?.results
            if (!similarShows.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                TMDBSectionHeader(
                    title = "Similar Shows",
                    onSeeAllClick = {},
                    showSeeAll = false
                )
                TMDBHorizontalList(
                    items = similarShows,
                    itemContent = { similar ->
                        TMDBTvShowItem(tvShow = similar, onClick = onShowClick)
                    }
                )
            }
        }
    }
}

@Composable
private fun TvShowsDetailsTopBar(
    title: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                // Circular Back Button
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

                // Title + Pulsing Crimson Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title.ifBlank { "TV Series" },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp,
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

            // Circular Share Button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHighDark)
                    .border(1.dp, GlassBorder, CircleShape)
                    .clickable { onShareClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = OnSurfaceDark,
                    modifier = Modifier.size(18.dp)
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
private fun SeasonCard(
    season: Season,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .width(125.dp)
            .clip(cornerShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cornerShape)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(SurfaceContainerLowestDark)
        ) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + season.posterPath,
                contentDescription = season.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Bottom subtle gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                SurfaceContainerLowestDark.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Season number badge if available
            val seasonNumber = season.seasonNumber ?: 0
            if (seasonNumber > 0) {
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceContainerLowestDark.copy(alpha = 0.85f))
                        .border(0.5.dp, GlassBorder, RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "S$seasonNumber",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = season.name ?: "Season",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceDark,
                    fontSize = 13.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val episodesText = "${season.episodeCount ?: 0} Episodes"
            Text(
                text = episodesText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
private fun TvShowInfoSection(show: TvShowDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        TMDBSectionHeader(
            title = "Information",
            onSeeAllClick = {},
            showSeeAll = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        val cardShape = RoundedCornerShape(16.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
                .background(SurfaceContainerDark)
                .border(1.dp, GlassBorder, cardShape)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status row with indicator
            val status = show.status
            if (!status.isNullOrBlank()) {
                val isOngoing = status.contains("Returning", ignoreCase = true) ||
                    status.contains("In Production", ignoreCase = true)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isOngoing) Color(0xFF34D399) else CrimsonLight)
                        )
                        Text(
                            text = status,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            val firstAirDate = show.firstAirDate
            if (!firstAirDate.isNullOrBlank()) {
                InfoRow("First Air Date", firstAirDate.formatDate())
            }

            val seasons = show.numberOfSeasons
            if (seasons != null && seasons > 0) {
                InfoRow("Seasons", "$seasons ${if (seasons == 1) "Season" else "Seasons"}")
            }

            val episodes = show.numberOfEpisodes
            if (episodes != null && episodes > 0) {
                InfoRow("Episodes", "$episodes Episodes")
            }

            val type = show.type
            if (!type.isNullOrBlank()) {
                InfoRow("Type", type)
            }

            val networks = show.networks?.mapNotNull { it?.name }?.joinToString(", ")
            if (!networks.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Network",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHighDark)
                            .border(1.dp, GlassBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = networks,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            }

            val productions = show.productionCompanies?.mapNotNull { it?.name }?.joinToString(", ")
            if (!productions.isNullOrBlank()) {
                InfoRow("Production", productions)
            }

            val language = show.originalLanguage
            if (!language.isNullOrBlank()) {
                InfoRow("Original Language", language.uppercase())
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

private fun shareTvShow(context: Context, title: String?, showId: Int?) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out \"$title\" on TMDB: https://www.themoviedb.org/tv/$showId"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share TV Series")
    context.startActivity(shareIntent)
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E13)
@Composable
private fun TvShowsDetailsContentPreview() {
    val mockShow = TvShowDetails(
        id = 1,
        name = "Law & Order: Special Victims Unit",
        tagline = "In the criminal justice system, sexually based offenses are considered especially heinous.",
        overview = "Led by the unwavering Captain Olivia Benson, NYPD’s Special Victims Unit unravels high-stakes cases across the city. Navigating moral labyrinths and courtroom battles, the team fights fiercely for survivors.",
        voteAverage = 8.8,
        firstAirDate = "1999-09-20",
        numberOfSeasons = 28,
        numberOfEpisodes = 602,
        status = "Returning Series",
        type = "Scripted Procedural",
        genres = listOf(Genre(1, "Crime"), Genre(2, "Drama"), Genre(3, "Mystery")),
        seasons = listOf(
            Season(id = 1, name = "Season 1", episodeCount = 22, seasonNumber = 1),
            Season(id = 2, name = "Season 2", episodeCount = 21, seasonNumber = 2),
            Season(id = 3, name = "Season 3", episodeCount = 23, seasonNumber = 3)
        )
    )

    TMDBTheme {
        TvShowsDetailsContent(
            uiState = TvShowsDetailsUiState(showDetails = mockShow),
            onBackClick = {},
            onShowClick = {},
            onCastClick = {},
            onTrailerClick = {},
            onSeasonClick = { _, _ -> }
        )
    }
}
