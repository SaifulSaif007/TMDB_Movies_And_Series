package com.saiful.movie.view.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.CrimsonLight
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.shared.model.Movies
import com.saiful.shared.utils.AppConstants
import java.util.Locale

@Composable
fun MovieListItem(
    movie: Movies,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    val posterShape = RoundedCornerShape(12.dp)
    val releaseYear = movie.releaseDate?.take(4)
    val overview = movie.overview
    val voteAverage = movie.voteAverage

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { onClick(movie.id) }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster Thumbnail
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(posterShape)
                    .background(SurfaceContainerLowestDark)
                    .border(1.dp, GlassBorder, posterShape)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Movie Details Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .padding(vertical = 4.dp, horizontal = 2.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = movie.title ?: "",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark,
                            fontSize = 16.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!overview.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = overview,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Rating & Metadata Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating Pill
                    if (voteAverage != null && voteAverage > 0.0) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHighDark)
                                .border(1.dp, GlassBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = NeonAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = String.format(Locale.US, "%.1f", voteAverage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "/ 10",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    // Release Year / Format Badge
                    if (!releaseYear.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CinemaCrimson.copy(alpha = 0.15f))
                                .border(1.dp, CinemaCrimson.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = releaseYear,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CrimsonLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun MovieListItemPreview() {
    TMDBTheme {
        MovieListItem(
            movie = Movies(
                id = 1,
                title = "Spider-Man: Brand New Day",
                overview = "Fighting crime full-time as Spider-Man in a world that doesn't remember him...",
                voteAverage = 8.5,
                releaseDate = "2026-05-01"
            ),
            onClick = {}
        )
    }
}
