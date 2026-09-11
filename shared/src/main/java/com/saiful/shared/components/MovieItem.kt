package com.saiful.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants

@Composable
fun TMDBMovieItem(
    movie: Movies,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .height(165.dp)
            .clickable { onClick(movie.id) },
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + movie.posterPath,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TMDBMovieItemPreview() {
    TMDBTheme {
        TMDBMovieItem(
            movie = Movies(
                id = 1,
                title = "Spider-Man: No Way Home",
                posterPath = null
            ),
            onClick = {}
        )
    }
}
