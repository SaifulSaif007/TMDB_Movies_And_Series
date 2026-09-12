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
import com.saiful.shared.model.TvShows
import com.saiful.shared.utils.AppConstants

@Composable
fun TMDBTvShowItem(
    tvShow: TvShows,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .height(165.dp)
            .clickable { onClick(tvShow.id) },
        shape = CardDefaults.shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + tvShow.posterPath,
            contentDescription = tvShow.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TMDBTvShowItemPreview() {
    TMDBTheme {
        TMDBTvShowItem(
            tvShow = TvShows(
                id = 1,
                name = "The Last of Us",
                posterPath = null
            ),
            onClick = {}
        )
    }
}
