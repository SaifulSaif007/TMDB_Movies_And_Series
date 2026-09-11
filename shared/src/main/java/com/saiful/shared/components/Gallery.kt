package com.saiful.shared.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.utils.AppConstants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TMDBGallery(
    images: List<String>,
    modifier: Modifier = Modifier,
    startIndex: Int = 0
) {
    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { images.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        AsyncImage(
            model = AppConstants.IMAGE_BASE_URL + AppConstants.ORIGINAL_SIZE + images[page],
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TMDBGalleryPreview() {
    TMDBTheme {
        TMDBGallery(
            images = listOf("image1.jpg", "image2.jpg")
        )
    }
}
