package com.saiful.movie.view.details

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.movie.model.Cast
import com.saiful.movie.model.MovieDetailsResponse
import com.saiful.shared.components.*
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onTrailerClick: (String) -> Unit,
    onCollectionClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.movieDetails?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        MovieDetailsContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            onMovieClick = onMovieClick,
            onCastClick = onCastClick,
            onTrailerClick = onTrailerClick,
            onCollectionClick = onCollectionClick
        )
    }
}

@Composable
fun MovieDetailsContent(
    uiState: MovieDetailsUiState,
    onMovieClick: (Int) -> Unit,
    onCastClick: (Int) -> Unit,
    onTrailerClick: (String) -> Unit,
    onCollectionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = uiState.movieDetails

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.height(250.dp)) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + movie?.backdropPath,
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
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + movie?.posterPath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Collection Card Button if present
        movie?.belongsToCollection?.let { collection ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onCollectionClick(collection.id ?: 0) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Part of the ${collection.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "View Collection",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = movie?.title ?: "",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "(${floatNumberFormatter(movie?.voteAverage?.toFloat())})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                movie?.genres?.filterNotNull()?.forEach { genre ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(genre.name ?: "") }
                    )
                }
            }

            Text(
                text = movie?.tagline ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie?.overview ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        MovieInfoSection(movie)

        if (uiState.cast.isNotEmpty()) {
            TMDBSectionHeader(title = "Cast", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.cast,
                itemContent = { cast ->
                    TMDBCastItem(
                        name = cast.name ?: "",
                        character = cast.character,
                        profilePath = cast.profilePath,
                        onClick = { onCastClick(cast.id) }
                    )
                }
            )
        }

        val trailers = movie?.videos?.results?.filter { it.type == "Trailer" || it.type == "Teaser" }
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

        if (uiState.recommendationsList.isNotEmpty()) {
            TMDBSectionHeader(title = "Recommendations", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.recommendationsList,
                itemContent = { recommendation ->
                    TMDBMovieItem(movie = recommendation, onClick = onMovieClick)
                }
            )
        }

        if (uiState.similarMoviesList.isNotEmpty()) {
            TMDBSectionHeader(title = "Similar Movies", onSeeAllClick = {}, showSeeAll = false)
            TMDBHorizontalList(
                items = uiState.similarMoviesList,
                itemContent = { similar ->
                    TMDBMovieItem(movie = similar, onClick = onMovieClick)
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun MovieInfoSection(movie: MovieDetailsResponse?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Information",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow("Budget", movie?.budget?.toLong()?.formatToShortNumber() ?: "")
        InfoRow("Revenue", movie?.revenue?.toLong()?.formatToShortNumber() ?: "")
        InfoRow("Status", movie?.status ?: "")
        InfoRow("Release Date", movie?.releaseDate?.formatDate() ?: "")
        InfoRow("Runtime", "${movie?.runtime} mins")
        InfoRow("Production", movie?.productionCompanies?.map { it?.name }?.joinToString(", ") ?: "")
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

@Preview(showBackground = true)
@Composable
private fun MovieInfoSectionPreview() {
    TMDBTheme {
        MovieInfoSection(
            movie = MovieDetailsResponse(
                budget = 200000000,
                revenue = 1900000000.0,
                status = "Released",
                releaseDate = "2021-12-17",
                runtime = 148
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsContentPreview() {
    val dummyMovie = Movies(
        id = 1,
        title = "Spider-Man: No Way Home",
        posterPath = "/1g0dhYEjmvl6Y7KEmUz9AUIXo7r.jpg",
        backdropPath = "/1g0dhYEjmvl6Y7KEmUz9AUIXo7r.jpg",
        voteAverage = 8.2
    )
    TMDBTheme {
        MovieDetailsContent(
            uiState = MovieDetailsUiState(
                movieDetails = MovieDetailsResponse(
                    title = "Spider-Man: No Way Home",
                    overview = "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a Super Hero. When he asks for help from Doctor Strange, the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.",
                    voteAverage = 8.2,
                    tagline = "The Multiverse unleashed.",
                    budget = 200000000,
                    revenue = 1900000000.0,
                    status = "Released",
                    releaseDate = "2021-12-17",
                    runtime = 148
                ),
                cast = listOf(
                    Cast(id = 1, name = "Tom Holland", character = "Peter Parker / Spider-Man", profilePath = null),
                    Cast(id = 2, name = "Zendaya", character = "MJ", profilePath = null)
                ),
                recommendationsList = listOf(dummyMovie, dummyMovie),
                similarMoviesList = listOf(dummyMovie, dummyMovie)
            ),
            onMovieClick = {},
            onCastClick = {},
            onTrailerClick = {},
            onCollectionClick = {}
        )
    }
}
