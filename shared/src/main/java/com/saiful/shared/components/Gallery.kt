package com.saiful.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.utils.AppConstants

@Composable
fun TMDBGallery(
    images: List<String>,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    pagerState: PagerState = rememberPagerState(
        initialPage = startIndex.coerceIn(0, (images.size - 1).coerceAtLeast(0)),
        pageCount = { images.size }
    )
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.ORIGINAL_SIZE + images[page],
                contentDescription = "Gallery image ${page + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E13)
@Composable
private fun TMDBGalleryPreview() {
    TMDBTheme {
        TMDBGallery(
            images = listOf("image1.jpg", "image2.jpg")
        )
    }
}
