package com.saiful.person.view.dashboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBPersonItem
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.model.Person
import com.saiful.shared.model.PersonCategory
import com.saiful.shared.utils.AppConstants
import java.util.Locale

@Composable
fun PersonDashboardScreen(
    viewModel: PersonDashboardVM,
    onPersonClick: (Int) -> Unit,
    onSeeAllClick: (PersonCategory) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    PersonDashboardContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onPersonClick = onPersonClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
fun PersonDashboardContent(
    modifier: Modifier = Modifier,
    uiState: PersonDashboardUiState,
    onPersonClick: (Int) -> Unit,
    onSeeAllClick: (PersonCategory) -> Unit
) {
    val isLoading = uiState.popularPersons.isEmpty() && uiState.trendingPersons.isEmpty()
    val spotlightPerson = remember(uiState.popularPersons) {
        uiState.popularPersons.randomOrNull() ?: uiState.trendingPersons.randomOrNull()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- 1. Top Cinematic Header Bar ---
        TopCinematicHeader()

        // --- 2. Scrollable Dashboard Content ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            if (isLoading) {
                PersonDashboardSkeleton()
            } else {
                // Featured Celebrity Spotlight Banner (random from popular ones)
                if (spotlightPerson != null) {
                    SpotlightCelebrityBanner(
                        person = spotlightPerson,
                        onPersonClick = onPersonClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    )
                }

                // Section 1: Popular People (2 horizontal rows of 10 items)
                if (uiState.popularPersons.isNotEmpty()) {
                    PersonTwoRowsSection(
                        title = "Popular People",
                        persons = uiState.popularPersons,
                        onPersonClick = onPersonClick,
                        onSeeAllClick = { onSeeAllClick(PersonCategory.POPULAR) }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 2: Trending People (2 horizontal rows of 10 items)
                if (uiState.trendingPersons.isNotEmpty()) {
                    PersonTwoRowsSection(
                        title = "Trending People",
                        persons = uiState.trendingPersons,
                        onPersonClick = onPersonClick,
                        onSeeAllClick = { onSeeAllClick(PersonCategory.TRENDING) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SpotlightCelebrityBanner(
    person: Person,
    onPersonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(20.dp)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
    ) {
        // Ambient Image Background
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.PROFILE_SIZE + person.profilePath,
            contentDescription = person.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Multi-layer Scrim Gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SurfaceContainerLowestDark.copy(alpha = 0.55f),
                            SurfaceContainerLowestDark.copy(alpha = 0.65f),
                            SurfaceContainerLowestDark.copy(alpha = 0.95f)
                        ),
                        startY = 0f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            SurfaceContainerLowestDark.copy(alpha = 0.85f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Spotlight Content Overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Spotlight badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceContainerLowestDark.copy(alpha = 0.85f))
                    .border(0.5.dp, GlassBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(CinemaCrimson.copy(alpha = pulseAlpha))
                )
                Text(
                    text = "SPOTLIGHT CELEBRITY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CrimsonLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp
                    )
                )
            }

            // Bottom Content Section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Popularity Badge
                if (person.popularity > 0.0) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonAmber.copy(alpha = 0.18f))
                            .border(1.dp, NeonAmber.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = NeonAmber,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "POPULARITY ${String.format(Locale.US, "%.0f", person.popularity)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.4.sp
                            )
                        )
                    }
                }

                // Celebrity Name
                Text(
                    text = person.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Known For / Department
                val knownForTitles = person.knownFor?.mapNotNull { it.title ?: it.name }?.takeIf { it.isNotEmpty() }
                val bioSnippet = if (knownForTitles != null) {
                    "Known for ${knownForTitles.take(3).joinToString(", ")}"
                } else {
                    person.knownForDepartment?.takeIf { it.isNotBlank() } ?: "Celebrity"
                }
                Text(
                    text = bioSnippet,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = OnSurfaceDark.copy(alpha = 0.82f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // CTA Button: View Profile
                Button(
                    onClick = { onPersonClick(person.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CinemaCrimson,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View Profile",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PersonTwoRowsSection(
    title: String,
    persons: List<Person>,
    onPersonClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TMDBSectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick
        )

        // First horizontal row (up to first 10 items)
        val firstRowItems = persons.take(10)
        TMDBHorizontalList(
            items = firstRowItems,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            itemContent = { person ->
                TMDBPersonItem(
                    person = person,
                    onClick = onPersonClick,
                    modifier = Modifier.width(120.dp)
                )
            }
        )

        // Second horizontal row (next 10 items, if available)
        if (persons.size > 10) {
            val secondRowItems = persons.subList(10, minOf(20, persons.size))
            Spacer(modifier = Modifier.height(6.dp))
            TMDBHorizontalList(
                items = secondRowItems,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                itemContent = { person ->
                    TMDBPersonItem(
                        person = person,
                        onClick = onPersonClick,
                        modifier = Modifier.width(120.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun TopCinematicHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowestDark.copy(alpha = 0.95f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(CinemaCrimson, Color(0xFF8B0000))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        val w = size.width
                        val h = size.height
                        drawRoundRect(
                            color = Color.White,
                            size = size,
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                        drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = Offset(w * 0.32f, h * 0.5f))
                        drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = Offset(w * 0.68f, h * 0.5f))
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Cine",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "Wave",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CinemaCrimson,
                            fontSize = 20.sp,
                            letterSpacing = (-0.5).sp
                        )
                    )
                }
            }

            // Action Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Notification Bell with Active Indicator Dot
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighDark)
                        .border(1.dp, GlassBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = OnSurfaceDark.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                    // Red indicator dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 7.dp, end = 7.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CinemaCrimson)
                    )
                }

                // Profile Avatar Chip
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(SurfaceContainerHighDark, SurfaceContainerDark)
                            )
                        )
                        .border(1.dp, GlassBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CW",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CinemaCrimson,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Hairline bottom divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(GlassBorder)
        )
    }
}

@Composable
private fun PersonDashboardSkeleton() {
    val shimmerColors = listOf(
        SurfaceContainerDark,
        SurfaceContainerHighDark,
        SurfaceContainerDark
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = androidx.compose.ui.geometry.Offset(translateAnim - 500f, 0f),
        end = androidx.compose.ui.geometry.Offset(translateAnim, 0f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Spotlight banner skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(brush)
        )

        // Section header skeleton
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(140.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )

        // Row 1 skeleton
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(brush)
                )
            }
        }

        // Row 2 skeleton
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(brush)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E13)
@Composable
private fun PersonDashboardContentPreview() {
    val dummyPersons = List(20) { index ->
        Person(
            id = index + 1,
            name = "Celebrity ${index + 1}",
            popularity = 90.0 - index,
            profilePath = null,
            knownForDepartment = "Acting"
        )
    }

    TMDBTheme {
        PersonDashboardContent(
            uiState = PersonDashboardUiState(
                popularPersons = dummyPersons,
                trendingPersons = dummyPersons
            ),
            onPersonClick = {},
            onSeeAllClick = {}
        )
    }
}
