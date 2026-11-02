package com.saiful.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants
import java.util.Locale

@Composable
fun TMDBMovieItem(
    movie: Movies,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .width(135.dp)
            .clickable { onClick(movie.id) }
    ) {
        // Poster Card with Atmospheric Border & Rating Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp)
                .clip(cornerShape)
                .background(SurfaceContainerDark)
                .border(1.dp, GlassBorder, cornerShape)
        ) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + movie.posterPath,
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Bottom subtle vignette gradient on poster
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                SurfaceContainerLowestDark.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Rating Badge at Top-Left
            if (movie.voteAverage != null && movie.voteAverage > 0.0) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .border(1.dp, NeonAmber.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = NeonAmber,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = String.format(Locale.US, "%.1f", movie.voteAverage),
                        color = NeonAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = movie.title ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark,
                fontSize = 13.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Subtitle: Year / Metadata
        val releaseYear = movie.releaseDate?.take(4)
        val subtitle = if (!releaseYear.isNullOrBlank()) {
            "$releaseYear • Movie"
        } else {
            "Movie"
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextMuted,
                fontSize = 11.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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
                posterPath = null,
                voteAverage = 8.4,
                releaseDate = "2021-12-15"
            ),
            onClick = {}
        )
    }
}
