package com.saiful.person.view.details

import android.content.Intent
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSecondary
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerHighestDark
import com.saiful.base.ui.theme.SurfaceContainerLowDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.person.model.PersonDetails
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBMovieItem
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.components.TMDBTvShowItem
import com.saiful.shared.model.Image
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun PersonDetailsScreen(
    personId: Int,
    viewModel: PersonDetailsVM,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit,
    onImageClick: (Int, List<Image>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(personId) {
        viewModel.fetchPersonDetails(personId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // Sticky Obsidian Top Bar
        PersonDetailsTopBar(
            title = uiState.personDetails?.name ?: "Celebrity Profile",
            onBackClick = onBackClick
        )

        // Main Scrollable Content
        if (uiState.personDetails == null) {
            PersonDetailsSkeleton()
        } else {
            PersonDetailsContent(
                uiState = uiState,
                onMovieClick = onMovieClick,
                onShowClick = onShowClick,
                onImageClick = onImageClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PersonDetailsTopBar(
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 19.sp,
                            letterSpacing = (-0.3).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
fun PersonDetailsContent(
    uiState: PersonDetailsUiState,
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit,
    onImageClick: (Int, List<Image>) -> Unit,
    modifier: Modifier = Modifier
) {
    val details = uiState.personDetails ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Actor Profile Header Card ---
        ActorProfileHeaderCard(details = details)

        // --- 2. Biography Section ---
        if (details.biography.isNotBlank()) {
            BiographySection(biography = details.biography)
        }

        // --- 3. Photos Gallery Rail ---
        val photos = uiState.personImageList?.profiles.orEmpty()
        if (photos.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TMDBSectionHeader(
                    title = "Photos",
                    onSeeAllClick = { onImageClick(0, photos) },
                    showSeeAll = photos.size > 1
                )
                TMDBHorizontalList(
                    items = photos,
                    itemContent = { image ->
                        TMDBGalleryItem(
                            image = image,
                            onClick = {
                                val index = photos.indexOf(image)
                                onImageClick(index, photos)
                            }
                        )
                    }
                )
            }
        }

        // --- 4. Movies Rail Section ---
        val movieCast = uiState.personMovieList?.cast.orEmpty()
        if (movieCast.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TMDBSectionHeader(
                    title = "Movies",
                    onSeeAllClick = {},
                    showSeeAll = false
                )
                TMDBHorizontalList(
                    items = movieCast.sortedByDescending { it.releaseDate },
                    itemContent = { movie ->
                        TMDBMovieItem(movie = movie, onClick = onMovieClick)
                    }
                )
            }
        }

        // --- 5. TV Shows Rail Section ---
        val tvCast = uiState.personShowsList?.cast.orEmpty()
        if (tvCast.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TMDBSectionHeader(
                    title = "TV Shows & Specials",
                    onSeeAllClick = {},
                    showSeeAll = false
                )
                TMDBHorizontalList(
                    items = tvCast,
                    itemContent = { show ->
                        TMDBTvShowItem(tvShow = show, onClick = onShowClick)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ActorProfileHeaderCard(details: PersonDetails) {
    val context = LocalContext.current
    var isFollowing by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLowDark)
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        // Subtle top-right ambient crimson glow
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(CinemaCrimson.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Celebrity Portrait Thumbnail with Tag Pill
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(155.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerDark)
                ) {
                    AsyncImage(
                        model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + details.profilePath,
                        contentDescription = details.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        SurfaceContainerLowestDark.copy(alpha = 0.85f)
                                    ),
                                    startY = 80f
                                )
                            )
                    )

                    val deptTag = details.department.takeIf { it.isNotBlank() } ?: "Actor"
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonAmber.copy(alpha = 0.95f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = deptTag.uppercase(Locale.US),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }

                // Quick Bio Metadata Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 155.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = details.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        val age = calculateAge(details.birthday)
                        val subText = buildString {
                            if (details.department.isNotBlank()) append(details.department)
                            if (age != null) {
                                if (isNotEmpty()) append(" · ")
                                append("Age $age")
                            }
                        }
                        if (subText.isNotBlank()) {
                            Text(
                                text = subText,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = CinemaCrimson,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Birthday Row
                        if (details.birthday.isNotBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CalendarIcon(tint = TextMuted)
                                Text(
                                    text = details.birthday.formatDate(),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = OnSurfaceDark,
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Birthplace Row
                        if (details.birthPlace.isNotBlank()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                LocationPinIcon(tint = TextMuted)
                                Text(
                                    text = details.birthPlace,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = OnSurfaceDark,
                                        fontSize = 12.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Popularity Pill Badge
                    if ((details.popularity ?: 0.0) > 0.0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(SurfaceContainerHighDark)
                                .border(0.5.dp, GlassBorder, RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = NeonAmber,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = String.format(Locale.US, "%.1f", details.popularity),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonAmber,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Quick CTA Buttons: Follow & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Follow Button
                Button(
                    onClick = { isFollowing = !isFollowing },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFollowing) SurfaceContainerHighDark else CinemaCrimson,
                        contentColor = if (isFollowing) NeonAmber else Color.White
                    ),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isFollowing) "Following" else "Follow",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }

                // Share Button
                Button(
                    onClick = {
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Check out ${details.name} on CineWave!"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share ${details.name}"))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerHighestDark,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Share",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BiographySection(biography: String) {
    var isBioExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Biography",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(CinemaCrimson)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { isBioExpanded = !isBioExpanded }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isBioExpanded) "Read less" else "Read more",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CinemaCrimson,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )
                Icon(
                    imageVector = if (isBioExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = CinemaCrimson,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceContainerLowDark)
                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = biography,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = OnSurfaceDark,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    ),
                    maxLines = if (isBioExpanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Bio summary curated via TMDB",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun TMDBGalleryItem(
    image: Image,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(115.dp)
            .height(165.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHighDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + image.filePath,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                SurfaceContainerLowestDark.copy(alpha = 0.5f)
                            ),
                            startY = 90f
                        )
                    )
            )
        }
    }
}

@Composable
private fun CalendarIcon(modifier: Modifier = Modifier, tint: Color = TextMuted) {
    Canvas(modifier = modifier.size(13.dp)) {
        val stroke = 1.2.dp.toPx()
        drawRoundRect(
            color = tint,
            topLeft = Offset(0f, size.height * 0.2f),
            size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.8f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = Stroke(stroke)
        )
        drawLine(
            color = tint,
            start = Offset(0f, size.height * 0.45f),
            end = Offset(size.width, size.height * 0.45f),
            strokeWidth = stroke
        )
        drawLine(
            color = tint,
            start = Offset(size.width * 0.28f, 0f),
            end = Offset(size.width * 0.28f, size.height * 0.25f),
            strokeWidth = stroke
        )
        drawLine(
            color = tint,
            start = Offset(size.width * 0.72f, 0f),
            end = Offset(size.width * 0.72f, size.height * 0.25f),
            strokeWidth = stroke
        )
    }
}

@Composable
private fun LocationPinIcon(modifier: Modifier = Modifier, tint: Color = TextMuted) {
    Canvas(modifier = modifier.size(13.dp)) {
        val stroke = 1.2.dp.toPx()
        val center = Offset(size.width / 2f, size.height * 0.35f)
        val radius = size.width * 0.3f
        drawCircle(color = tint, radius = radius, center = center, style = Stroke(stroke))
        drawCircle(color = tint, radius = radius * 0.35f, center = center)
        drawLine(
            color = tint,
            start = Offset(center.x, center.y + radius),
            end = Offset(center.x, size.height * 0.9f),
            strokeWidth = stroke
        )
    }
}

private fun calculateAge(birthday: String?): Int? {
    if (birthday.isNullOrBlank()) return null
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val birthDate = sdf.parse(birthday) ?: return null
        val birthCal = Calendar.getInstance().apply { time = birthDate }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        if (age in 0..130) age else null
    } catch (e: Exception) {
        null
    }
}

@Composable
private fun PersonDetailsSkeleton() {
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header card placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(brush)
        )

        // Bio section placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(brush)
        )

        // Photos row placeholder
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(115.dp)
                        .height(165.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF131318)
@Composable
private fun PersonDetailsContentPreview() {
    TMDBTheme {
        PersonDetailsContent(
            uiState = PersonDetailsUiState(
                personDetails = PersonDetails(
                    id = 1,
                    name = "Tom Holland",
                    biography = "Thomas Stanley Holland (born 1 June 1996) is an English actor. His accolades include a BAFTA Film Award...",
                    birthday = "1996-06-01",
                    birthPlace = "London, England, UK",
                    gender = 2,
                    department = "Acting",
                    popularity = 98.4
                )
            ),
            onMovieClick = {},
            onShowClick = {},
            onImageClick = { _, _ -> }
        )
    }
}
