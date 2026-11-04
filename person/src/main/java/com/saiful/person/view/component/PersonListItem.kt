package com.saiful.person.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.OnSurfaceVariantDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerLowDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.SurfaceVariantDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.shared.model.KnownFor
import com.saiful.shared.model.Person
import com.saiful.shared.utils.AppConstants
import java.util.Locale

@Composable
fun PersonListItem(
    person: Person,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    rank: Int? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceContainerLowDark)
            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
            .clickable { onClick(person.id) }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Celebrity Portrait Thumbnail with Rank Badge Overlay
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .height(118.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainerDark)
            ) {
                AsyncImage(
                    model = AppConstants.IMAGE_BASE_URL + AppConstants.PROFILE_SIZE + person.profilePath,
                    contentDescription = person.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient overlay at bottom of image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                                startY = 60f
                            )
                        )
                )

                // Rank badge in top-left
                if (rank != null) {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerLowestDark.copy(alpha = 0.85f))
                            .border(0.5.dp, GlassBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            if (rank <= 3) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = NeonAmber,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                            Text(
                                text = "#$rank",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (rank <= 3) NeonAmber else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            // Celebrity Bio & Filmography Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Name & Star Icon Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = person.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 17.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Popularity",
                        tint = NeonAmber,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(start = 4.dp)
                    )
                }

                // Department tag + Popularity rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val dept = person.knownForDepartment?.takeIf { it.isNotBlank() } ?: "Acting"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceVariantDark)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = dept.uppercase(Locale.US),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CinemaCrimson,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    )

                    Text(
                        text = String.format(Locale.US, "%.1f Rating", person.popularity),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OnSurfaceVariantDark,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }

                // Known For filmography string
                val knownForTitles = person.knownFor
                    ?.mapNotNull { it.title ?: it.name }
                    ?.filter { it.isNotBlank() }
                    ?.joinToString(", ")

                if (!knownForTitles.isNullOrBlank()) {
                    Text(
                        text = knownForTitles,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun PersonListItemPreview() {
    TMDBTheme {
        PersonListItem(
            person = Person(
                id = 1,
                name = "Tom Holland",
                popularity = 98.4,
                knownForDepartment = "Acting",
                profilePath = null,
                knownFor = listOf(
                    KnownFor(
                        id = 1,
                        title = "Spider-Man: Brand New Day",
                        voteAverage = 8.8
                    ),
                    KnownFor(
                        id = 2,
                        title = "Uncharted",
                        voteAverage = 7.1
                    )
                )
            ),
            rank = 1,
            onClick = {}
        )
    }
}
