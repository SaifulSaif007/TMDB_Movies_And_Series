package com.saiful.movie.view.details

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.saiful.shared.components.AuthGateBottomSheet
import com.saiful.shared.components.RatingDialog
import kotlinx.coroutines.flow.collectLatest
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
import com.saiful.movie.model.Cast
import com.saiful.movie.model.MovieDetailsResponse
import com.saiful.shared.components.TMDBCastItem
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBMovieItem
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.components.TMDBTrailerItem
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import com.saiful.shared.utils.formatToShortNumber
import java.util.Locale

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onTrailerClick: (String) -> Unit,
    onCollectionClick: (Int) -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showRatingDialog by remember { mutableStateOf(false) }
    var showAuthGate by remember { mutableStateOf(false) }
    var authGateFeature by remember { mutableStateOf("") }

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    LaunchedEffect(Unit) {
        viewModel.showMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MovieDetailsContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onBackClick = onBackClick,
            onMovieClick = onMovieClick,
            onCastClick = onCastClick,
            onTrailerClick = onTrailerClick,
            onCollectionClick = onCollectionClick,
            onRateClick = {
                if (isLoggedIn) {
                    showRatingDialog = true
                } else {
                    authGateFeature = "rate this movie"
                    showAuthGate = true
                }
            },
            onFavoriteClick = {
                if (isLoggedIn) {
                    viewModel.toggleFavorite(movieId)
                } else {
                    authGateFeature = "add to Favorites"
                    showAuthGate = true
                }
            },
            onWatchlistClick = {
                if (isLoggedIn) {
                    viewModel.toggleWatchlist(movieId)
                } else {
                    authGateFeature = "add to your Watchlist"
                    showAuthGate = true
                }
            }
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    if (showRatingDialog) {
        RatingDialog(
            currentRating = uiState.userRating,
            onConfirm = { rating ->
                showRatingDialog = false
                viewModel.submitMovieRating(movieId, rating)
            },
            onDelete = {
                showRatingDialog = false
                viewModel.deleteMovieRating(movieId)
            },
            onDismiss = { showRatingDialog = false }
        )
    }

    if (showAuthGate) {
        AuthGateBottomSheet(
            feature = authGateFeature,
            onLoginClick = {
                showAuthGate = false
                onNavigateToProfile()
            },
            onDismiss = { showAuthGate = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailsContent(
    uiState: MovieDetailsUiState,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onTrailerClick: (String) -> Unit,
    onCollectionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onRateClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onWatchlistClick: () -> Unit = {}
) {
    val movie = uiState.movieDetails
    val context = LocalContext.current
    val trailers = movie?.videos?.results?.filter { it.type == "Trailer" || it.type == "Teaser" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- 1. Top Cinematic App Bar ---
        MovieDetailsTopBar(
            title = movie?.title ?: "",
            isFavorite = uiState.isFavorite,
            isWatchlisted = uiState.isWatchlisted,
            onBackClick = onBackClick,
            onFavoriteClick = onFavoriteClick,
            onWatchlistClick = onWatchlistClick,
            onShareClick = {
                shareMovie(context, movie?.title ?: "Movie", movie?.id ?: 0)
            }
        )

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
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + movie?.backdropPath,
                    contentDescription = movie?.title,
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
            }

            // Inset Overlapping Poster & Quick Actions Row
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
                // Movie Poster with Rating Badge
                val posterShape = RoundedCornerShape(16.dp)
                Box(
                    modifier = Modifier
                        .width(115.dp)
                        .aspectRatio(2f / 3f)
                        .clip(posterShape)
                        .background(SurfaceContainerDark)
                        .border(2.dp, GlassBorder, posterShape)
                ) {
                    AsyncImage(
                        model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + movie?.posterPath,
                        contentDescription = movie?.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Poster Rating Pill
                    if (movie?.voteAverage != null && movie.voteAverage > 0.0) {
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
                                text = String.format(Locale.US, "%.1f", movie.voteAverage),
                                color = NeonAmber,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Quick Action Buttons
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaCrimson,
                            contentColor = Color.White,
                            disabledContainerColor = SurfaceContainerHighDark,
                            disabledContentColor = TextMuted
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Watch Trailer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Secondary Franchise / Rate / Share Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Rate Action Button
                        val isRated = uiState.userRating != null
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isRated) NeonAmber.copy(alpha = 0.15f) else SurfaceContainerHighDark)
                                .border(
                                    1.dp,
                                    if (isRated) NeonAmber.copy(alpha = 0.6f) else GlassBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onRateClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rate",
                                    tint = if (isRated) NeonAmber else CrimsonLight,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = if (isRated) "★ ${String.format(Locale.US, "%.1f", uiState.userRating)}" else "Rate",
                                    color = if (isRated) NeonAmber else OnSurfaceDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Share Action
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceContainerHighDark)
                                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                                .clickable {
                                    shareMovie(context, movie?.title ?: "Movie", movie?.id ?: 0)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = CrimsonLight,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Share",
                                    color = OnSurfaceDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Collection Action (if belongs to collection)
                        val collectionId = movie?.belongsToCollection?.id
                        if (collectionId != null && collectionId > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceContainerHighDark)
                                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                                    .clickable { onCollectionClick(collectionId) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Franchise",
                                    color = NeonAmber,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Franchise / Collection Banner ---
            movie?.belongsToCollection?.let { collection ->
                CollectionBannerCard(
                    collectionName = collection.name ?: "Franchise Collection",
                    onClick = { onCollectionClick(collection.id ?: 0) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- Title, Rating & Metadata Section ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Title and Rating Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = movie?.title ?: "",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurfaceDark,
                            fontSize = 24.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    if (movie?.voteAverage != null && movie.voteAverage > 0.0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(SurfaceContainerHighDark)
                                .border(1.dp, NeonAmber.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
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
                                text = String.format(Locale.US, "%.1f", movie.voteAverage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonAmber,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                // Genre Pills
                if (!movie?.genres.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        movie?.genres?.filterNotNull()?.forEach { genre ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(SurfaceContainerHighDark)
                                    .border(1.dp, GlassBorder, RoundedCornerShape(999.dp))
                                    .padding(horizontal = 12.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = genre.name ?: "",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = OnSurfaceDark.copy(alpha = 0.85f),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Tagline with Crimson Left Accent
                if (!movie?.tagline.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.5.dp)
                                .height(22.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(CinemaCrimson)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "\"${movie?.tagline}\"",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                color = CrimsonLight,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        )
                    }
                }

                // Overview Synopsis
                if (!movie?.overview.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = movie?.overview ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = OnSurfaceDark.copy(alpha = 0.82f),
                            lineHeight = 22.sp,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Information Section (Production & Specs) ---
            MovieInfoCardSection(movie)

            // --- Cast Section ---
            if (uiState.cast.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                TMDBSectionHeader(title = "Cast & Crew", onSeeAllClick = {}, showSeeAll = false)
                TMDBHorizontalList(
                    items = uiState.cast,
                    itemContent = { cast ->
                        TMDBCastItem(
                            name = cast.name ?: "",
                            character = cast.character ?: "",
                            profilePath = cast.profilePath,
                            onClick = { onCastClick(cast.id) }
                        )
                    }
                )
            }

            // --- Trailers & Videos Section ---
            if (!trailers.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                TMDBSectionHeader(title = "Trailers & Clips", onSeeAllClick = {}, showSeeAll = false)
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

            // --- Recommendations Section ---
            if (uiState.recommendationsList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                TMDBSectionHeader(title = "Recommendations", onSeeAllClick = {}, showSeeAll = false)
                TMDBHorizontalList(
                    items = uiState.recommendationsList,
                    itemContent = { recommendation ->
                        TMDBMovieItem(movie = recommendation, onClick = onMovieClick)
                    }
                )
            }

            // --- Similar Movies Section ---
            if (uiState.similarMoviesList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                TMDBSectionHeader(title = "Similar Movies", onSeeAllClick = {}, showSeeAll = false)
                TMDBHorizontalList(
                    items = uiState.similarMoviesList,
                    itemContent = { similar ->
                        TMDBMovieItem(movie = similar, onClick = onMovieClick)
                    }
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsTopBar(
    title: String,
    isFavorite: Boolean = false,
    isWatchlisted: Boolean = false,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit = {},
    onWatchlistClick: () -> Unit = {},
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
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
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
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceDark,
                        fontSize = 16.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Top Bar Actions: Favorite, Watchlist, Share
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Favorite Action
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isFavorite) CinemaCrimson.copy(alpha = 0.2f) else SurfaceContainerHighDark)
                        .border(
                            1.dp,
                            if (isFavorite) CinemaCrimson.copy(alpha = 0.7f) else GlassBorder,
                            CircleShape
                        )
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) CinemaCrimson else OnSurfaceDark,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Watchlist Action
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isWatchlisted) NeonAmber.copy(alpha = 0.2f) else SurfaceContainerHighDark)
                        .border(
                            1.dp,
                            if (isWatchlisted) NeonAmber.copy(alpha = 0.7f) else GlassBorder,
                            CircleShape
                        )
                        .clickable { onWatchlistClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isWatchlisted) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Watchlist",
                        tint = if (isWatchlisted) NeonAmber else OnSurfaceDark,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Share Action
                Box(
                    modifier = Modifier
                        .size(36.dp)
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
                        modifier = Modifier.size(17.dp)
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
private fun CollectionBannerCard(
    collectionName: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        SurfaceContainerHighDark,
                        SurfaceContainerDark
                    )
                )
            )
            .border(1.dp, GlassBorder, shape)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "FRANCHISE UNIVERSE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CinemaCrimson,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        fontSize = 10.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = collectionName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceDark,
                        fontSize = 13.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "View",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CinemaCrimson,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = CinemaCrimson,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun MovieInfoCardSection(movie: MovieDetailsResponse?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TMDBSectionHeader(
            title = "Information",
            onSeeAllClick = {},
            showSeeAll = false,
            modifier = Modifier.padding(horizontal = 0.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        val cardShape = RoundedCornerShape(16.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
                .background(SurfaceContainerDark)
                .border(1.dp, GlassBorder, cardShape)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            InfoDataRow(label = "Budget", value = movie?.budget?.toLong()?.formatToShortNumber() ?: "-")
            InfoDivider()
            InfoDataRow(label = "Revenue", value = movie?.revenue?.toLong()?.formatToShortNumber() ?: "-")
            InfoDivider()
            InfoDataRow(label = "Status", value = movie?.status ?: "Released")
            InfoDivider()
            InfoDataRow(label = "Release Date", value = movie?.releaseDate?.formatDate() ?: "-")
            InfoDivider()
            InfoDataRow(label = "Runtime", value = if (movie?.runtime != null && movie.runtime > 0) "${movie.runtime} mins" else "-")
            InfoDivider()
            InfoDataRow(
                label = "Production",
                value = movie?.productionCompanies?.mapNotNull { it?.name }?.joinToString(", ") ?: "-"
            )
        }
    }
}

@Composable
private fun InfoDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextMuted,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = OnSurfaceDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InfoDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(GlassBorder)
    )
}

private fun shareMovie(context: Context, title: String, movieId: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out $title on TMDB: https://www.themoviedb.org/movie/$movieId"
        )
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Share $title"))
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsContentPreview() {
    val dummyMovie = Movies(
        id = 1,
        title = "Spider-Man: Brand New Day",
        posterPath = null,
        backdropPath = null,
        voteAverage = 8.5
    )
    TMDBTheme {
        MovieDetailsContent(
            uiState = MovieDetailsUiState(
                movieDetails = MovieDetailsResponse(
                    title = "Spider-Man: Brand New Day",
                    overview = "Peter Parker fights crime full-time in a world that doesn't remember him.",
                    voteAverage = 8.5,
                    tagline = "A brand new day starts now.",
                    budget = 225000000,
                    revenue = 2500000000.0,
                    status = "Released",
                    releaseDate = "2026-07-29",
                    runtime = 145
                ),
                cast = listOf(
                    Cast(id = 1, name = "Tom Holland", character = "Peter Parker / Spider-Man", profilePath = null),
                    Cast(id = 2, name = "Zendaya", character = "MJ", profilePath = null)
                ),
                recommendationsList = listOf(dummyMovie, dummyMovie),
                similarMoviesList = listOf(dummyMovie, dummyMovie)
            ),
            onBackClick = {},
            onMovieClick = {},
            onCastClick = {},
            onTrailerClick = {},
            onCollectionClick = {}
        )
    }
}
