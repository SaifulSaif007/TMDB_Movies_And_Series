package com.saiful.movie.view.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.movie.view.components.MovieListItem
import com.saiful.shared.model.MovieCategory
import com.saiful.shared.components.TMDBErrorView
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.floatNumberFormatter
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    category: MovieCategory,
    viewModel: ListVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val movies = viewModel.movieList.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category.value) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        MovieListContent(
            modifier = Modifier.padding(padding),
            movies = movies,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
fun MovieListContent(
    movies: LazyPagingItems<Movies>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = movies.itemCount,
                key = movies.itemKey { it.id },
                contentType = movies.itemContentType { "movie" }
            ) { index ->
                movies[index]?.let { movie ->
                    MovieListItem(movie = movie, onClick = onMovieClick)
                }
            }

            when (val state = movies.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        TMDBErrorView(message = state.error.message ?: "Error loading more")
                    }
                }

                else -> {}
            }
        }

        when (val state = movies.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView()
            }

            is LoadState.Error -> {
                TMDBErrorView(message = state.error.message ?: "Error loading movies")
            }

            else -> {}
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MovieListContentPreview() {
    val movies = listOf(
        Movies(id = 1, title = "The Avengers", voteAverage = 8.0, posterPath = null),
        Movies(id = 2, title = "Avengers: Age of Ultron", voteAverage = 7.5, posterPath = null)
    )
    val pagingData = PagingData.from(movies)
    val fakeDataFlow = flowOf(pagingData)
    val lazyPagingItems = fakeDataFlow.collectAsLazyPagingItems()

    TMDBTheme {
        MovieListContent(
            movies = lazyPagingItems,
            onMovieClick = {}
        )
    }
}
