package com.saiful.tvshows.view.season

import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.LocalContext
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
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import com.saiful.tvshows.model.Episode
import com.saiful.tvshows.model.SeasonDetails
import java.util.Locale

@Composable
fun TvShowSeasonScreen(
    showId: Int,
    seasonNo: Int,
    viewModel: ShowSeasonVM,
    onBackClick: () -> Unit
) {
    val seasonDetails by viewModel.seasonDetails.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(showId, seasonNo) {
        viewModel.fetchSeason(showId, seasonNo)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- 1. Sticky Top Cinematic App Bar ---
        TvShowSeasonTopBar(
            title = seasonDetails?.name ?: "Season Details",
            onBackClick = onBackClick,
            onShareClick = {
                shareSeason(context, seasonDetails?.name ?: "Season", showId, seasonNo)
            }
        )

        // --- 2. Season Content & Episodes List ---
        val season = seasonDetails
        if (season == null) {
            TMDBLoadingView(modifier = Modifier.fillMaxSize())
        } else {
            TvShowSeasonContent(
                season = season,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TvShowSeasonTopBar(
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
                        text = title,
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
private fun TvShowSeasonContent(
    season: SeasonDetails,
    modifier: Modifier = Modifier
) {
    var sortAscending by remember { mutableStateOf(true) }
    val displayEpisodes = remember(season.episodes, sortAscending) {
        if (sortAscending) {
            season.episodes.sortedBy { it.episodeNumber }
        } else {
            season.episodes.sortedByDescending { it.episodeNumber }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- 1. Season Summary Hero Card ---
        item(key = "season_hero_card") {
            SeasonHeroCard(season = season)
        }

        // --- 2. Episodes Section Header & Sort Switcher ---
        item(key = "episodes_header") {
            EpisodesHeaderWithSort(
                episodeCount = season.episodes.size,
                sortAscending = sortAscending,
                onSortChange = { sortAscending = it }
            )
        }

        // --- 3. Episode Cards List ---
        items(
            items = displayEpisodes,
            key = { it.id }
        ) { episode ->
            EpisodeCardItem(episode = episode)
        }
    }
}

@Composable
private fun SeasonHeroCard(
    season: SeasonDetails,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    val posterShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Season Poster Thumbnail
                Box(
                    modifier = Modifier
                        .width(105.dp)
                        .height(154.dp)
                        .clip(posterShape)
                        .background(SurfaceContainerLowestDark)
                        .border(1.dp, GlassBorder, posterShape)
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

                    // Season Pill Badge
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
                            text = "S${season.seasonNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    }
                }

                // Season Details Info
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(154.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Season badge tag
                        val tagText = if (season.seasonNumber == 1) "DEBUT SEASON" else "SEASON ${season.seasonNumber}"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CinemaCrimson.copy(alpha = 0.15f))
                                .border(1.dp, CinemaCrimson.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tagText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CrimsonLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }

                        // Season Title
                        Text(
                            text = season.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 18.sp,
                                letterSpacing = (-0.3).sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Air Date & Episode count
                        val airDateFormatted = (season.airDate as? String)?.takeIf { it.isNotBlank() }?.formatDate() ?: ""
                        val metadataText = if (airDateFormatted.isNotBlank()) {
                            "$airDateFormatted • ${season.episodes.size} Episodes"
                        } else {
                            "${season.episodes.size} Episodes"
                        }
                        Text(
                            text = metadataText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        )

                        // Season Overview snippet
                        if (season.overview.isNotBlank()) {
                            Text(
                                text = season.overview,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = OnSurfaceDark.copy(alpha = 0.80f),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    // Format specification badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FormatChip("HD")
                        FormatChip("5.1 AUDIO")
                        FormatChip("SERIES")
                    }
                }
            }
        }
    }
}

@Composable
private fun FormatChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(SurfaceContainerHighDark)
            .border(0.5.dp, GlassBorder, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        )
    }
}

@Composable
private fun EpisodesHeaderWithSort(
    episodeCount: Int,
    sortAscending: Boolean,
    onSortChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Section Title + Episodes Count Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CinemaCrimson)
            )

            Text(
                text = "Episodes",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainerHighDark)
                    .border(0.5.dp, GlassBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$episodeCount Total",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp
                    )
                )
            }
        }

        // Sort Toggle (Earliest / Latest)
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceContainerDark)
                .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SortTab(
                title = "Earliest",
                selected = sortAscending,
                onClick = { onSortChange(true) }
            )
            SortTab(
                title = "Latest",
                selected = !sortAscending,
                onClick = { onSortChange(false) }
            )
        }
    }
}

@Composable
private fun SortTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (selected) SurfaceContainerHighDark else Color.Transparent)
            .border(
                1.dp,
                if (selected) GlassBorder else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 9.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (selected) Color.White else TextMuted,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
private fun EpisodeCardItem(
    episode: Episode,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    val thumbnailShape = RoundedCornerShape(10.dp)
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { isExpanded = !isExpanded }
            .animateContentSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Still Image Thumbnail with 16:9 Aspect Ratio
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(76.dp)
                    .clip(thumbnailShape)
                    .background(SurfaceContainerLowestDark)
                    .border(1.dp, GlassBorder, thumbnailShape)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + episode.stillPath,
                    contentDescription = episode.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Bottom subtle vignette gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
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

                // Runtime badge at bottom right
                val runtime = episode.runtime
                if (runtime != null && runtime > 0) {
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.BottomEnd)
                            .clip(RoundedCornerShape(3.dp))
                            .background(SurfaceContainerLowestDark.copy(alpha = 0.85f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "${runtime}m",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            // Episode Details Column
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                // Episode Number & Name
                Text(
                    text = "${episode.episodeNumber}. ${episode.name}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Air Date & Rating Metadata
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val airDateFormatted = (episode.airDate as? String)?.takeIf { it.isNotBlank() }?.formatDate() ?: ""
                    if (airDateFormatted.isNotBlank()) {
                        Text(
                            text = airDateFormatted,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }

                    val voteAverage = episode.voteAverage
                    if (voteAverage > 0.0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = NeonAmber,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = String.format(Locale.US, "%.1f", voteAverage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonAmber,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Overview snippet (expandable)
                if (episode.overview.isNotBlank()) {
                    Text(
                        text = episode.overview,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceDark.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        ),
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }
        }
    }
}

private fun shareSeason(context: Context, title: String, showId: Int, seasonNo: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out $title on TMDB: https://www.themoviedb.org/tv/$showId/season/$seasonNo"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Season")
    context.startActivity(shareIntent)
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E13)
@Composable
private fun TvShowSeasonContentPreview() {
    val mockSeason = SeasonDetails(
        id = 1,
        airDate = "1999-09-20",
        name = "Season 1",
        overview = "The detectives of the Special Victims Unit investigate vicious felonies and sexual assaults in New York City.",
        posterPath = "",
        seasonNumber = 1,
        episodes = listOf(
            Episode(
                id = 1,
                name = "Payback",
                overview = "Detectives Stabler and Benson investigate the murder and dismemberment of a taxi driver who was a former Serbian soldier.",
                airDate = "1999-09-20",
                episodeNumber = 1,
                runtime = 45,
                seasonNumber = 1,
                stillPath = "",
                voteAverage = 8.2,
                voteCount = 45
            ),
            Episode(
                id = 2,
                name = "A Single Life",
                overview = "Benson and Stabler run into multiple dead ends while investigating the case of a woman who fell to her death from her apartment balcony.",
                airDate = "1999-09-27",
                episodeNumber = 2,
                runtime = 44,
                seasonNumber = 1,
                stillPath = "",
                voteAverage = 8.0,
                voteCount = 38
            )
        )
    )

    TMDBTheme {
        TvShowSeasonContent(
            season = mockSeason
        )
    }
}
