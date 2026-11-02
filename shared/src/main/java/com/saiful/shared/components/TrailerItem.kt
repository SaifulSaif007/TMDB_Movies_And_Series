package com.saiful.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import com.saiful.shared.utils.AppConstants

@Composable
fun TMDBTrailerItem(
    name: String,
    key: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerShape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .width(240.dp)
            .clip(cornerShape)
            .background(SurfaceContainerHighDark)
            .border(1.dp, GlassBorder, cornerShape)
            .clickable { onClick(key) }
    ) {
        // Thumbnail with 16:9 Aspect Ratio and Glowing Play Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(SurfaceContainerDark)
        ) {
            AsyncImage(
                model = AppConstants.YOUTUBE_IMAGE_URL_PREFIX + key + AppConstants.YOUTUBE_IMAGE_URL_SUFFIX,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark semi-transparent scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            // Center Glowing Play Button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(CinemaCrimson)
                    .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Trailer",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Top-Left Badge: "Trailer"
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
                    .clip(RoundedCornerShape(4.dp))
                    .background(CinemaCrimson.copy(alpha = 0.90f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "TRAILER",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Title text below
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceDark,
                    fontSize = 12.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Official Video",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TMDBTrailerItemPreview() {
    TMDBTheme {
        TMDBTrailerItem(
            name = "Spider-Man: Brand New Day - Official Trailer",
            key = "key",
            onClick = {}
        )
    }
}
