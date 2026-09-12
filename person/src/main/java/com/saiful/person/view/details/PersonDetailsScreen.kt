package com.saiful.person.view.details

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.person.model.PersonDetails
import com.saiful.shared.components.*
import com.saiful.shared.model.Image
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsScreen(
    personId: Int,
    viewModel: PersonDetailsVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit,
    onImageClick: (Int, List<Image>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(personId) {
        viewModel.fetchPersonDetails(personId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.personDetails?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        PersonDetailsContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onMovieClick = onMovieClick,
            onShowClick = onShowClick,
            onImageClick = onImageClick
        )
    }
}

@Composable
fun PersonDetailsContent(
    uiState: PersonDetailsUiState,
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit,
    onImageClick: (Int, List<Image>) -> Unit,
    modifier: Modifier = Modifier
) {
    val details = uiState.personDetails

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + details?.profilePath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = details?.name ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Born: ${details?.birthday?.formatDate()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "From: ${details?.birthPlace}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (!details?.biography.isNullOrEmpty()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Biography",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = details?.biography ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (uiState.personImageList?.profiles != null && uiState.personImageList.profiles.isNotEmpty()) {
            TMDBSectionHeader(title = "Photos", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.personImageList.profiles,
                itemContent = { image ->
                    TMDBGalleryItem(
                        image = image,
                        onClick = {
                            val index = uiState.personImageList.profiles.indexOf(image)
                            onImageClick(index, uiState.personImageList.profiles)
                        }
                    )
                }
            )
        }

        if (uiState.personMovieList?.cast != null && uiState.personMovieList.cast.isNotEmpty()) {
            TMDBSectionHeader(title = "Movies", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.personMovieList.cast.sortedByDescending { it.releaseDate },
                itemContent = { movie ->
                    TMDBMovieItem(movie = movie, onClick = onMovieClick)
                }
            )
        }

        if (uiState.personShowsList?.cast != null && uiState.personShowsList.cast.isNotEmpty()) {
            TMDBSectionHeader(title = "TV Shows", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.personShowsList.cast,
                itemContent = { show ->
                    TMDBTvShowItem(tvShow = show, onClick = onShowClick)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TMDBGalleryItem(
    image: Image,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(100.dp)
            .height(150.dp)
            .padding(4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + image.filePath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonDetailsContentPreview() {
    TMDBTheme {
        PersonDetailsContent(
            uiState = PersonDetailsUiState(
                personDetails = PersonDetails(
                    id = 1,
                    name = "Tom Cruise",
                    biography = "Thomas Cruise Mapother IV is an American actor and producer...",
                    birthday = "1962-07-03",
                    birthPlace = "Syracuse, New York, USA",
                    gender = 2
                )
            ),
            onMovieClick = {},
            onShowClick = {},
            onImageClick = { _, _ -> }
        )
    }
}
