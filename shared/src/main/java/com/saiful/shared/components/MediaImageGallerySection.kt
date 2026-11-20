package com.saiful.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.model.Image
import com.saiful.shared.model.MediaImagesResponse
import com.saiful.shared.utils.AppConstants
import java.util.Locale

@Composable
fun MediaImageGallerySection(
    images: MediaImagesResponse?,
    onImageClick: (Int, List<Image>) -> Unit,
    modifier: Modifier = Modifier
) {
    val backdrops = images?.backdrops.orEmpty().filter { it.filePath.isNotBlank() }
    val posters = images?.posters.orEmpty().filter { it.filePath.isNotBlank() }

    if (backdrops.isEmpty() && posters.isEmpty()) return

    // 0 = Backdrops, 1 = Posters
    var selectedTab by remember(backdrops.isNotEmpty(), posters.isNotEmpty()) {
        mutableIntStateOf(if (backdrops.isNotEmpty()) 0 else 1)
    }

    val currentList = if (selectedTab == 0 && backdrops.isNotEmpty()) backdrops else posters
    val isBackdrop = selectedTab == 0 && backdrops.isNotEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Section Header
        TMDBSectionHeader(
            title = "Photos & Gallery",
            onSeeAllClick = {
                if (currentList.isNotEmpty()) {
                    onImageClick(0, currentList)
                }
            },
            showSeeAll = currentList.size > 1
        )

        // Tab Selector (if both backdrops and posters are available)
        if (backdrops.isNotEmpty() && posters.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GalleryTabChip(
                    text = "Backdrops (${backdrops.size})",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                GalleryTabChip(
                    text = "Posters (${posters.size})",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }

        // Horizontal Gallery Strip
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(currentList) { index, image ->
                if (isBackdrop) {
                    BackdropGalleryItem(
                        image = image,
                        onClick = { onImageClick(index, currentList) }
                    )
                } else {
                    PosterGalleryItem(
                        image = image,
                        onClick = { onImageClick(index, currentList) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (isSelected) CinemaCrimson.copy(alpha = 0.22f) else SurfaceContainerHighDark)
            .border(
                1.dp,
                if (isSelected) CinemaCrimson.copy(alpha = 0.7f) else GlassBorder,
                shape
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) CinemaCrimson else OnSurfaceDark.copy(alpha = 0.85f),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
private fun BackdropGalleryItem(
    image: Image,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .width(204.dp)
            .height(115.dp)
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.BACKDROP_SIZE + image.filePath,
            contentDescription = "Backdrop",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            SurfaceContainerLowestDark.copy(alpha = 0.55f)
                        ),
                        startY = 60f
                    )
                )
        )

        // Rating pill (if available)
        if (image.voteAverage > 0.0) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .border(1.dp, NeonAmber.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
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
                    text = String.format(Locale.US, "%.1f", image.voteAverage),
                    color = NeonAmber,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PosterGalleryItem(
    image: Image,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .width(115.dp)
            .height(165.dp)
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + image.filePath,
            contentDescription = "Poster",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            SurfaceContainerLowestDark.copy(alpha = 0.55f)
                        ),
                        startY = 90f
                    )
                )
        )

        // Rating pill (if available)
        if (image.voteAverage > 0.0) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .border(1.dp, NeonAmber.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 5.dp, vertical = 2.dp),
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
                    text = String.format(Locale.US, "%.1f", image.voteAverage),
                    color = NeonAmber,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E13)
@Composable
private fun MediaImageGallerySectionPreview() {
    TMDBTheme {
        MediaImageGallerySection(
            images = MediaImagesResponse(
                id = 1,
                backdrops = listOf(
                    Image(filePath = "/path1.jpg", voteAverage = 8.5),
                    Image(filePath = "/path2.jpg", voteAverage = 7.9)
                ),
                posters = listOf(
                    Image(filePath = "/poster1.jpg", voteAverage = 9.0)
                )
            ),
            onImageClick = { _, _ -> }
        )
    }
}
