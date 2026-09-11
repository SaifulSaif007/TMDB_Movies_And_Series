package com.saiful.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.model.Movies

@Composable
fun <T> TMDBHorizontalList(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp)
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement
    ) {
        items(items) { item ->
            itemContent(item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TMDBHorizontalListPreview() {
    TMDBTheme {
        TMDBHorizontalList(
            items = listOf(
                Movies(id = 1, title = "Movie 1", posterPath = null),
                Movies(id = 2, title = "Movie 2", posterPath = null),
                Movies(id = 3, title = "Movie 3", posterPath = null)
            ),
            itemContent = { movie ->
                TMDBMovieItem(movie = movie, onClick = {})
            }
        )
    }
}
