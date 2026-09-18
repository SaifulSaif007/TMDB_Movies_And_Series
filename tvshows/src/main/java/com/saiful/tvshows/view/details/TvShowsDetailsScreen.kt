package com.saiful.tvshows.view.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saiful.tvshows.model.*
import com.saiful.shared.components.*
import com.saiful.shared.utils.*

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.showDetails?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        TvShowsDetailsContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            onShowClick = onShowClick,
            onCastClick = onCastClick,
            onTrailerClick = onTrailerClick,
            onSeasonClick = onSeasonClick
        )
    }
}

@Composable
fun TvShowsDetailsContent(
    uiState: TvShowsDetailsUiState,
    onShowClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onTrailerClick: (String) -> Unit,
    onSeasonClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val show = uiState.showDetails

    if (show == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.height(250.dp)) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + (show.backdropPath ?: ""),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Card(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .width(100.dp)
                    .height(150.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + (show.posterPath ?: ""),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = show.name ?: "",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "(${floatNumberFormatter(show.voteAverage?.toFloat())})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                show.genres?.filterNotNull()?.forEach { genre ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(genre.name ?: "") }
                    )
                }
            }

            if (!show.tagline.isNullOrEmpty()) {
                Text(
                    text = show.tagline,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = show.overview ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        TvShowInfoSection(show)

        if (!show.seasons.isNullOrEmpty()) {
            TMDBSectionHeader(title = "Seasons", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = show.seasons,
                itemContent = { season ->
                    Card(
                        modifier = Modifier
                            .width(120.dp)
                            .padding(4.dp)
                            .clickable { onSeasonClick(show.id ?: 0, season.seasonNumber ?: 0) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + (season.posterPath ?: ""),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = season.name ?: "",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            )
        }

        if (uiState.showCasts?.cast != null && uiState.showCasts.cast.isNotEmpty()) {
            TMDBSectionHeader(title = "Cast", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.showCasts.cast,
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

        val trailers = show.videos?.results?.filter { it.type == "Trailer" || it.type == "Teaser" }
        if (!trailers.isNullOrEmpty()) {
            TMDBSectionHeader(title = "Trailers", onSeeAllClick = {}, showSeeAll = false)
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

        if (uiState.recommendations?.results != null && uiState.recommendations.results.isNotEmpty()) {
            TMDBSectionHeader(title = "Recommendations", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.recommendations.results,
                itemContent = { recommendation ->
                    TMDBTvShowItem(tvShow = recommendation, onClick = onShowClick)
                }
            )
        }

        if (uiState.similarShows?.results != null && uiState.similarShows.results.isNotEmpty()) {
            TMDBSectionHeader(title = "Similar Shows", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.similarShows.results,
                itemContent = { similar ->
                    TMDBTvShowItem(tvShow = similar, onClick = onShowClick)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TvShowInfoSection(show: TvShowDetails) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Information",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow("Status", show.status ?: "")
        InfoRow("First Air Date", show.firstAirDate?.formatDate() ?: "")
        InfoRow("Seasons", show.numberOfSeasons?.toString() ?: "")
        InfoRow("Episodes", show.numberOfEpisodes?.toString() ?: "")
        InfoRow("Type", show.type ?: "")
        InfoRow("Production", show.productionCompanies?.map { it?.name }?.joinToString(", ") ?: "")
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
