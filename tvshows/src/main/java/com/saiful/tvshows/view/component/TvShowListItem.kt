package com.saiful.tvshows.view.component

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
import com.saiful.shared.model.TvShows
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import java.util.Locale

@Composable
fun TvShowListItem(
    show: TvShows,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    val posterShape = RoundedCornerShape(12.dp)
    val airYear = show.firstAirDate?.take(4)
    val firstAirDate = show.firstAirDate
    val overview = show.overview
    val voteAverage = show.voteAverage

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { onClick(show.id) }
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
                    .width(108.dp)
                    .height(154.dp)
                    .clip(posterShape)
                    .background(SurfaceContainerLowestDark)
                    .border(1.dp, GlassBorder, posterShape)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + show.posterPath,
                    contentDescription = show.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Bottom subtle vignette gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    SurfaceContainerLowestDark.copy(alpha = 0.8f)
                                )
                            )
                        )
                )

                // Format Pill Badge (e.g. 4K HDR)
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(4.dp))
                        .background(SurfaceContainerLowestDark.copy(alpha = 0.85f))
                        .border(0.5.dp, GlassBorder, RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "4K HDR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            // TV Show Details Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(154.dp)
                    .padding(vertical = 4.dp, horizontal = 2.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = show.name ?: "",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark,
                            fontSize = 16.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val subText = if (!airYear.isNullOrBlank()) "$airYear • TV SHOW" else "TV SHOW"
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    if (!overview.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = overview,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Rating & Metadata Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
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
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Release Date / Spec Badge
                    val badgeText = if (!firstAirDate.isNullOrBlank()) firstAirDate.formatDate() else "Series"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CinemaCrimson.copy(alpha = 0.12f))
                            .border(1.dp, CinemaCrimson.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CrimsonLight,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                letterSpacing = 0.3.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun TvShowListItemPreview() {
    TMDBTheme {
        TvShowListItem(
            show = TvShows(
                id = 1,
                name = "Link Click",
                overview = "Using superpowers to enter their clientele's photos one by one, Cheng Xiaoshi and Lu Guang fulfill dangerous past requests.",
                voteAverage = 8.7,
                firstAirDate = "2021-04-30",
                posterPath = null
            ),
            onClick = {}
        )
    }
}
