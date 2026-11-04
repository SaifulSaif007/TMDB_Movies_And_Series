package com.saiful.person.view.list

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.OnSurfaceVariantDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.person.view.component.PersonListItem
import com.saiful.shared.model.KnownFor
import com.saiful.shared.model.Person
import com.saiful.shared.model.PersonCategory
import kotlinx.coroutines.flow.flowOf

@Composable
fun PersonListScreen(
    category: PersonCategory,
    viewModel: PersonListVM,
    onBackClick: () -> Unit,
    onPersonClick: (Int) -> Unit
) {
    val persons = viewModel.personList.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // Sticky Top Cinematic App Bar
        PersonListTopBar(
            title = "${category.value} People",
            onBackClick = onBackClick
        )

        // Live Metric Context Bar
        PersonMetricContextBar()

        // Paging List Content
        PersonListContent(
            persons = persons,
            onPersonClick = onPersonClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PersonListTopBar(
    title: String,
    onBackClick: () -> Unit
) {
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
            // Left: Back button + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Back Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHighDark)
                        .border(1.dp, GlassBorder, CircleShape)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OnSurfaceDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title with Crimson Pulse Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp,
                            letterSpacing = (-0.3).sp
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(CinemaCrimson)
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
private fun PersonMetricContextBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(CinemaCrimson)
            )
            Text(
                text = "LIVE STARPOWER RANK",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp,
                    fontSize = 11.sp
                )
            )
        }

        Text(
            text = "Updated hourly",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextMuted,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun PersonListContent(
    persons: LazyPagingItems<Person>,
    onPersonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (val refreshState = persons.loadState.refresh) {
            is LoadState.Loading -> {
                PersonListSkeleton()
            }

            is LoadState.Error -> {
                PersonListErrorState(
                    errorMessage = refreshState.error.message ?: "Failed to load people",
                    onRetry = { persons.retry() }
                )
            }

            else -> {
                if (persons.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No people found",
                            style = MaterialTheme.typography.bodyLarge.copy(color = TextMuted)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 4.dp,
                            bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            count = persons.itemCount,
                            key = persons.itemKey { it.id },
                            contentType = persons.itemContentType { "person" }
                        ) { index ->
                            persons[index]?.let { person ->
                                PersonListItem(
                                    person = person,
                                    rank = index + 1,
                                    onClick = onPersonClick
                                )
                            }
                        }

                        // Append load states (infinite scrolling pagination)
                        when (val appendState = persons.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = CinemaCrimson,
                                            modifier = Modifier.size(28.dp),
                                            strokeWidth = 2.5.dp
                                        )
                                    }
                                }
                            }

                            is LoadState.Error -> {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = appendState.error.message ?: "Error loading more",
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                        )
                                        Button(
                                            onClick = { persons.retry() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = SurfaceContainerHighDark,
                                                contentColor = CinemaCrimson
                                            ),
                                            shape = RoundedCornerShape(20.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Refresh,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(text = "Retry", fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonListErrorState(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerLowDark)
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(CinemaCrimson.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = CinemaCrimson,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "Failed to load people",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 13.sp
                )
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaCrimson,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Try Again", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PersonListSkeleton() {
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(138.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLowDark)
                    .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image placeholder
                    Box(
                        modifier = Modifier
                            .width(96.dp)
                            .height(118.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(brush)
                    )

                    // Text placeholders
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(brush)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(brush)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(12.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(brush)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun PersonListContentPreview() {
    val dummyPersons = List(10) { index ->
        Person(
            id = index + 1,
            name = "Celebrity ${index + 1}",
            popularity = 95.0 - index,
            knownForDepartment = "Acting",
            profilePath = null,
            knownFor = listOf(
                KnownFor(
                    id = 1,
                    title = "Movie Blockbuster",
                    voteAverage = 8.5
                )
            )
        )
    }

    TMDBTheme {
        PersonListContent(
            persons = flowOf(PagingData.from(dummyPersons)).collectAsLazyPagingItems(),
            onPersonClick = {}
        )
    }
}
