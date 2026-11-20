package com.saiful.shared.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.CrimsonBright
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.shared.model.ReviewAuthorDetails
import com.saiful.shared.model.ReviewItem
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.formatDate
import java.util.Locale

@Composable
fun CommunityReviewsSection(
    reviews: List<ReviewItem>,
    modifier: Modifier = Modifier,
    totalReviews: Int = reviews.size
) {
    var selectedReview by remember { mutableStateOf<ReviewItem?>(null) }
    var showAllReviewsSheet by remember { mutableStateOf(false) }

    val displayCount = if (totalReviews > 0) totalReviews else reviews.size

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Section Header with review count
        TMDBSectionHeader(
            title = if (displayCount > 0) "Community Reviews ($displayCount)" else "Community Reviews",
            onSeeAllClick = {
                showAllReviewsSheet = true
            },
            showSeeAll = reviews.isNotEmpty()
        )

        if (reviews.isEmpty()) {
            EmptyReviewsCard(modifier = Modifier.padding(horizontal = 16.dp))
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(
                        review = review,
                        onClick = { selectedReview = review }
                    )
                }
            }
        }
    }

    // Single Review Detail Bottom Sheet
    selectedReview?.let { review ->
        ReviewDetailBottomSheet(
            review = review,
            totalReviews = displayCount,
            onDismiss = { selectedReview = null },
            onViewAllClick = {
                selectedReview = null
                showAllReviewsSheet = true
            }
        )
    }

    // All Reviews Bottom Sheet (triggered by "See All")
    if (showAllReviewsSheet && reviews.isNotEmpty()) {
        AllReviewsBottomSheet(
            reviews = reviews,
            totalReviews = displayCount,
            onDismiss = { showAllReviewsSheet = false }
        )
    }
}

@Composable
private fun ReviewCard(
    review: ReviewItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    val author = review.author.ifBlank { review.authorDetails?.username ?: "Anonymous" }
    val rating = review.authorDetails?.rating
    val avatarUrl = getAvatarUrl(review.authorDetails?.avatarPath)
    val dateString = if (review.createdAt.length >= 10) {
        review.createdAt.take(10).formatDate()
    } else {
        review.createdAt
    }

    Box(
        modifier = modifier
            .width(285.dp)
            .height(165.dp)
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Avatar + Author Info + Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    ReviewerAvatar(
                        avatarUrl = avatarUrl,
                        name = author,
                        size = 36.dp
                    )

                    Column {
                        Text(
                            text = author,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark,
                                fontSize = 14.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (dateString.isNotBlank()) {
                            Text(
                                text = dateString,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Rating Pill (if given)
                if (rating != null && rating > 0.0) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHighDark)
                            .border(1.dp, NeonAmber.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp),
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
                            text = String.format(Locale.US, "%.1f", rating),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // Middle: Review Excerpt
            Text(
                text = review.content.trim(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = OnSurfaceDark.copy(alpha = 0.82f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Bottom Prompt: Read full review
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Read full review",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CinemaCrimson,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = CinemaCrimson,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyReviewsCard(modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark.copy(alpha = 0.7f))
            .border(1.dp, GlassBorder, cardShape)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.RateReview,
                contentDescription = null,
                tint = TextMuted.copy(alpha = 0.6f),
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "No community reviews yet",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceDark
                )
            )
            Text(
                text = "Be the first to share your thoughts on this title.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 12.sp
                )
            )
        }
    }
}

/**
 * All Reviews Bottom Sheet listing all community reviews
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllReviewsBottomSheet(
    reviews: List<ReviewItem>,
    totalReviews: Int,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceContainerLowestDark,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(CinemaCrimson)
                    )
                    Text(
                        text = "Community Reviews",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark,
                            fontSize = 18.sp
                        )
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainerHighDark)
                            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$totalReviews",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = OnSurfaceDark
                    )
                }
            }

            // Hairline Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(GlassBorder)
            )

            // Scrollable List of All Reviews
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(reviews) { review ->
                    AllReviewsItemCard(review = review)
                }
            }
        }
    }
}

@Composable
private fun AllReviewsItemCard(
    review: ReviewItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cardShape = RoundedCornerShape(16.dp)
    val author = review.author.ifBlank { review.authorDetails?.username ?: "Anonymous" }
    val rating = review.authorDetails?.rating
    val avatarUrl = getAvatarUrl(review.authorDetails?.avatarPath)
    val dateString = if (review.createdAt.length >= 10) {
        review.createdAt.take(10).formatDate()
    } else {
        review.createdAt
    }
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Avatar + Author Info + Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    ReviewerAvatar(
                        avatarUrl = avatarUrl,
                        name = author,
                        size = 40.dp
                    )

                    Column {
                        Text(
                            text = author,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark,
                                fontSize = 15.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (dateString.isNotBlank()) {
                            Text(
                                text = "Written on $dateString",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Rating Badge
                if (rating != null && rating > 0.0) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonAmber.copy(alpha = 0.15f))
                            .border(1.dp, NeonAmber.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = NeonAmber,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = String.format(Locale.US, "%.1f", rating),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // Review Content
            val content = review.content.trim()
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurfaceDark.copy(alpha = 0.88f),
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                ),
                maxLines = if (isExpanded) Int.MAX_VALUE else 5,
                overflow = TextOverflow.Ellipsis
            )

            // Footer Actions: Read more / Show less & TMDB link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (content.length > 200) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { isExpanded = !isExpanded }
                            .padding(vertical = 4.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = if (isExpanded) "Show less" else "Read more",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = CinemaCrimson,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = CinemaCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (review.url.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(review.url))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "TMDB",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewDetailBottomSheet(
    review: ReviewItem,
    totalReviews: Int = 1,
    onDismiss: () -> Unit,
    onViewAllClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val author = review.author.ifBlank { review.authorDetails?.username ?: "Anonymous" }
    val rating = review.authorDetails?.rating
    val avatarUrl = getAvatarUrl(review.authorDetails?.avatarPath)
    val dateString = if (review.createdAt.length >= 10) {
        review.createdAt.take(10).formatDate()
    } else {
        review.createdAt
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceContainerLowestDark,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ReviewerAvatar(
                        avatarUrl = avatarUrl,
                        name = author,
                        size = 46.dp
                    )

                    Column {
                        Text(
                            text = author,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark,
                                fontSize = 17.sp
                            )
                        )
                        if (dateString.isNotBlank()) {
                            Text(
                                text = "Written on $dateString",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                if (rating != null && rating > 0.0) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonAmber.copy(alpha = 0.15f))
                            .border(1.dp, NeonAmber.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = NeonAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = String.format(Locale.US, "%.1f / 10", rating),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(GlassBorder)
            )

            // Full Review Content
            Text(
                text = review.content.trim(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurfaceDark.copy(alpha = 0.90f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            )

            // Open on TMDB button (if URL exists)
            if (review.url.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(review.url))
                            context.startActivity(intent)
                        } catch (_: Exception) { }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerHighDark,
                        contentColor = OnSurfaceDark
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = CinemaCrimson
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Review on TMDB",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            // View all reviews button (if more than 1 review)
            if (totalReviews > 1) {
                Button(
                    onClick = onViewAllClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CinemaCrimson.copy(alpha = 0.16f),
                        contentColor = CinemaCrimson
                    )
                ) {
                    Text(
                        text = "See all $totalReviews reviews",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = CinemaCrimson
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewerAvatar(
    avatarUrl: String?,
    name: String,
    size: androidx.compose.ui.unit.Dp
) {
    if (!avatarUrl.isNullOrBlank()) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = name,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(1.dp, GlassBorder, CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        // Monogram fallback
        val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "U"
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(CinemaCrimson, CrimsonBright)
                    )
                )
                .border(1.dp, GlassBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.42f).sp
            )
        }
    }
}

/**
 * Normalizes TMDB avatar path which may be relative, gravatar, or standard URL.
 */
private fun getAvatarUrl(avatarPath: String?): String? {
    if (avatarPath.isNullOrBlank()) return null
    return when {
        avatarPath.startsWith("/https:") || avatarPath.startsWith("/http:") -> avatarPath.removePrefix("/")
        avatarPath.startsWith("http://") || avatarPath.startsWith("https://") -> avatarPath
        else -> AppConstants.IMAGE_BASE_URL + AppConstants.PROFILE_SIZE + avatarPath
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E13)
@Composable
private fun CommunityReviewsSectionPreview() {
    TMDBTheme {
        CommunityReviewsSection(
            reviews = listOf(
                ReviewItem(
                    id = "1",
                    author = "CinematicFan",
                    authorDetails = ReviewAuthorDetails(
                        username = "CinematicFan",
                        rating = 9.0
                    ),
                    content = "An absolute masterpiece of modern cinema. The visual effects and sound design are second to none.",
                    createdAt = "2024-05-12T14:30:00.000Z",
                    url = "https://themoviedb.org"
                )
            )
        )
    }
}
