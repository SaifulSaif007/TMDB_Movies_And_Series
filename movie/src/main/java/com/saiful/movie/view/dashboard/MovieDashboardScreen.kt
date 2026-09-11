package com.saiful.movie.view.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.movie.model.MovieCategory
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBImageSlider
import com.saiful.shared.components.TMDBMovieItem
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.model.Movies

@Composable
fun MovieDashboardScreen(
    viewModel: DashboardVM,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (MovieCategory) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    MovieDashboardContent(
        uiState = uiState,
        onMovieClick = onMovieClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
fun MovieDashboardContent(
    uiState: DashboardUiState,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: (MovieCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        if (uiState.sliderMovies.isNotEmpty()) {
            TMDBImageSlider(
                movies = uiState.sliderMovies,
                onItemClick = onMovieClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        MovieSection(
            title = "Popular",
            movies = uiState.popularMovies,
            onMovieClick = onMovieClick,
            onSeeAllClick = { onSeeAllClick(MovieCategory.POPULAR) }
        )

        MovieSection(
            title = "Now Playing",
            movies = uiState.nowPlayingMovies,
            onMovieClick = onMovieClick,
            onSeeAllClick = { onSeeAllClick(MovieCategory.NOW_PLAYING) }
        )

        MovieSection(
            title = "Top Rated",
            movies = uiState.topRatedMovies,
            onMovieClick = onMovieClick,
            onSeeAllClick = { onSeeAllClick(MovieCategory.TOP_RATED) }
        )

        MovieSection(
            title = "Upcoming",
            movies = uiState.upcomingMovies,
            onMovieClick = onMovieClick,
            onSeeAllClick = { onSeeAllClick(MovieCategory.UPCOMING) }
        )
    }
}

@Composable
private fun MovieSection(
    title: String,
    movies: List<Movies>,
    onMovieClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column {
        TMDBSectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick
        )
        TMDBHorizontalList(
            items = movies,
            itemContent = { movie ->
                TMDBMovieItem(
                    movie = movie,
                    onClick = onMovieClick
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieSectionPreview() {
    val dummyMovie = Movies(
        id = 1,
        title = "Spider-Man: No Way Home",
        posterPath = "/1g0dhYEjmvl6Y7KEmUz9AUIXo7r.jpg",
        voteAverage = 8.2
    )
    TMDBTheme {
        MovieSection(
            title = "Popular Movies",
            movies = listOf(dummyMovie, dummyMovie, dummyMovie),
            onMovieClick = {},
            onSeeAllClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDashboardContentPreview() {
    val dummyMovie = Movies(
        id = 1,
        title = "Spider-Man: No Way Home",
        posterPath = "/1g0dhYEjmvl6Y7KEmUz9AUIXo7r.jpg",
        backdropPath = "/1g0dhYEjmvl6Y7KEmUz9AUIXo7r.jpg",
        voteAverage = 8.2
    )
    TMDBTheme {
        MovieDashboardContent(
            uiState = DashboardUiState(
                popularMovies = listOf(dummyMovie, dummyMovie, dummyMovie),
                nowPlayingMovies = listOf(dummyMovie, dummyMovie),
                topRatedMovies = listOf(dummyMovie, dummyMovie, dummyMovie),
                upcomingMovies = listOf(dummyMovie),
                sliderMovies = listOf(dummyMovie, dummyMovie)
            ),
            onMovieClick = {},
            onSeeAllClick = {}
        )
    }
}
