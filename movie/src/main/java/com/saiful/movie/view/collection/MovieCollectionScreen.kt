package com.saiful.movie.view.collection

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
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
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.movie.model.MovieCollection
import com.saiful.shared.components.TMDBErrorView
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import java.util.Locale

@Composable
fun MovieCollectionScreen(
    collectionId: Int,
    viewModel: CollectionVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val collection by viewModel.collections.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(collectionId) {
        viewModel.fetchCollections(collectionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- Sticky Cinematic Top App Bar ---
        MovieCollectionTopBar(
            title = collection?.name ?: "Movie Collection",
            onBackClick = onBackClick,
            onShareClick = {
                collection?.let {
                    shareCollection(context, it.name ?: "Movie Collection", it.id)
                }
            }
        )

        // --- Screen Body Content with Loading / Error Handling ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                isLoading && collection == null -> {
                    TMDBLoadingView(modifier = Modifier.fillMaxSize())
                }

                collection != null -> {
                    MovieCollectionContent(
                        collection = collection!!,
                        onMovieClick = onMovieClick
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TMDBErrorView(message = "Unable to load collection details.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchCollections(collectionId) },
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
            }
        }
    }
}

@Composable
private fun MovieCollectionTopBar(
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
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Circular Back Button & Collection Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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

            Spacer(modifier = Modifier.width(8.dp))

            // Right: Circular Share Button
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
                    modifier = Modifier.size(16.dp)
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
fun MovieCollectionContent(
    collection: MovieCollection,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val movies = remember(collection.parts) {
        collection.parts?.sortedBy { it.releaseDate.orEmpty() } ?: emptyList()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianCanvas),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Hero Backdrop Banner ---
        item {
            CollectionHeroBanner(
                collection = collection
            )
        }

        // --- 2. Collection Overview & Metadata ---
        item {
            CollectionOverviewSection(
                collection = collection,
                movies = movies
            )
        }

        // --- 3. Movies in Collection Header ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Movies in this collection",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = OnSurfaceDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                // Films Count Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceContainerHighDark)
                        .border(1.dp, GlassBorder, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${movies.size} Films",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // --- 4. Franchise Movies Cards ---
        itemsIndexed(
            items = movies,
            key = { _, movie -> movie.id }
        ) { index, movie ->
            CollectionMovieCard(
                index = index + 1,
                movie = movie,
                onClick = { onMovieClick(movie.id) }
            )
        }
    }
}

@Composable
private fun CollectionHeroBanner(
    collection: MovieCollection
) {
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .clip(cardShape)
            .background(SurfaceContainerLowestDark)
            .border(1.dp, GlassBorder, cardShape)
    ) {
        // Backdrop Image
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + collection.backdropPath,
            contentDescription = collection.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.35f),
                            ObsidianCanvas.copy(alpha = 0.95f)
                        ),
                        startY = 40f
                    )
                )
        )

        // Top-Left Category Tag Pill
        Box(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.65f))
                .border(1.dp, GlassBorder, CircleShape)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
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
                    text = "FRANCHISE COLLECTION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceDark.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun CollectionOverviewSection(
    collection: MovieCollection,
    movies: List<Movies>
) {
    val validRatings = movies.mapNotNull { it.voteAverage }.filter { it > 0.0 }
    val avgRating = if (validRatings.isNotEmpty()) validRatings.average() else null
    val releaseYears = movies.mapNotNull { it.releaseDate?.take(4) }.filter { it.isNotBlank() }
    val releaseSpan = when {
        releaseYears.size >= 2 -> "${releaseYears.first()} – ${releaseYears.last()}"
        releaseYears.size == 1 -> releaseYears.first()
        else -> null
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Title
        Text(
            text = collection.name ?: "",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = OnSurfaceDark,
                fontSize = 24.sp,
                letterSpacing = (-0.5).sp
            )
        )

        // Metadata Tags Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Count Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(SurfaceContainerHighDark)
                    .border(1.dp, GlassBorder, RoundedCornerShape(999.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${movies.size} Movies",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceDark.copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                )
            }

            // Average Rating Pill
            if (avgRating != null) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceContainerHighDark)
                        .border(1.dp, NeonAmber.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = NeonAmber,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = String.format(Locale.US, "%.1f Avg", avgRating),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Release Span Pill
            if (!releaseSpan.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceContainerHighDark)
                        .border(1.dp, GlassBorder, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = releaseSpan,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Overview Text
        val overview = collection.overview
        if (!overview.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = overview,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurfaceDark.copy(alpha = 0.82f),
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                )
            )
        }
    }
}

@Composable
private fun CollectionMovieCard(
    index: Int,
    movie: Movies,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(14.dp)
    val posterShape = RoundedCornerShape(10.dp)
    val releaseYear = movie.releaseDate?.take(4)
    val overview = movie.overview
    val voteAverage = movie.voteAverage
    val releaseDate = movie.releaseDate

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Poster Thumbnail with #Index Badge
            Box(
                modifier = Modifier
                    .width(76.dp)
                    .aspectRatio(2f / 3f)
                    .clip(posterShape)
                    .background(SurfaceContainerLowestDark)
                    .border(1.dp, GlassBorder, posterShape)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Index Pill
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "#$index",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OnSurfaceDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            // Movie Details Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(114.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Title and Release Year
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = movie.title ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark,
                                fontSize = 14.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        if (!releaseYear.isNullOrBlank()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = releaseYear,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    // Overview
                    if (!overview.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = overview,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Rating & Specs Footer Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    if (voteAverage != null && voteAverage > 0.0) {
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
                            Text(
                                text = "/ 10",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Release Date badge
                    if (!releaseDate.isNullOrBlank()) {
                        Text(
                            text = releaseDate.formatDate(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun shareCollection(context: Context, name: String, collectionId: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        val url = if (collectionId > 0) {
            "https://www.themoviedb.org/collection/$collectionId"
        } else ""
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out the $name on TMDB $url".trim()
        )
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Share $name"))
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun MovieCollectionContentPreview() {
    TMDBTheme {
        MovieCollectionContent(
            collection = MovieCollection(
                id = 531241,
                name = "Spider-Man (MCU) Collection",
                overview = "A collection of films following Peter Parker as he navigates adolescence while taking on the responsibilities of being Spider-Man in a world filled with superheroes.",
                backdropPath = "/spider_backdrop.jpg",
                posterPath = "/spider_poster.jpg",
                parts = listOf(
                    Movies(
                        id = 1,
                        title = "Spider-Man: Homecoming",
                        overview = "Following the events of Captain America: Civil War, Peter Parker, with the help of his mentor Tony Stark, tries to balance high school life with battling the Vulture.",
                        voteAverage = 7.3,
                        releaseDate = "2017-07-05",
                        posterPath = null
                    ),
                    Movies(
                        id = 2,
                        title = "Spider-Man: Far From Home",
                        overview = "Peter Parker and his friends go on a summer trip to Europe. However, they are recruited by Nick Fury to uncover the mystery of elemental creature attacks.",
                        voteAverage = 7.4,
                        releaseDate = "2019-06-28",
                        posterPath = null
                    ),
                    Movies(
                        id = 3,
                        title = "Spider-Man: No Way Home",
                        overview = "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a superhero.",
                        voteAverage = 8.2,
                        releaseDate = "2021-12-15",
                        posterPath = null
                    )
                )
            ),
            onMovieClick = {}
        )
    }
}
