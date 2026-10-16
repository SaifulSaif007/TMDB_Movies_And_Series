package com.saiful.movie.view.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.movie.model.MovieCollection
import com.saiful.movie.view.components.MovieListItem
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCollectionScreen(
    collectionId: Int,
    viewModel: CollectionVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val collection by viewModel.collections.collectAsState()

    LaunchedEffect(collectionId) {
        viewModel.fetchCollections(collectionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = collection?.name ?: "Collection") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        collection?.let {
            MovieCollectionContent(
                modifier = Modifier.padding(padding),
                collection = it,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
private fun MovieCollectionContent(
    collection: MovieCollection,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + collection.backdropPath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        item {
            Text(
                text = collection.name ?: "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = collection.overview ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "Movies in this collection",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(collection.parts ?: emptyList()) { movie ->
            MovieListItem(
                movie = movie,
                onClick = onMovieClick
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun MovieCollectionContentPreview() {
    TMDBTheme {
        MovieCollectionContent(
            collection = MovieCollection(
                id = 1,
                name = "Avengers Collection",
                overview = "A superhero film series based on the Marvel Comics superhero team of the same name.",
                parts = listOf(
                    Movies(id = 1, title = "The Avengers", voteAverage = 8.0, posterPath = null),
                    Movies(id = 2, title = "Avengers: Age of Ultron", voteAverage = 7.5, posterPath = null)
                )
            ),
            onMovieClick = {}
        )
    }
}
