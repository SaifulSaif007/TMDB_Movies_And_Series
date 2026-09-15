package com.saiful.tvshows.view.season

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.utils.AppConstants
import com.saiful.tvshows.model.Episode
import com.saiful.tvshows.model.SeasonDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowSeasonScreen(
    showId: Int,
    seasonNo: Int,
    viewModel: ShowSeasonVM,
    onBackClick: () -> Unit
) {
    val seasonDetails by viewModel.seasonDetails.collectAsState()

    LaunchedEffect(showId, seasonNo) {
        viewModel.fetchSeason(showId, seasonNo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = seasonDetails?.name ?: "Season Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        seasonDetails?.let {
            TvShowSeasonContent(
                modifier = Modifier.padding(padding),
                season = it
            )
        }
    }
}

@Composable
private fun TvShowSeasonContent(
    season: SeasonDetails,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    AsyncImage(
                        model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + season.posterPath,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = season.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Air Date: ${season.airDate}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = season.overview,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        items(season.episodes) { episode ->
            EpisodeItem(episode = episode)
        }
    }
}

@Composable
private fun EpisodeItem(episode: Episode) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + episode.stillPath,
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(60.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = "${episode.episodeNumber}. ${episode.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = episode.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
