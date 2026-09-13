package com.saiful.shared.view.gallery

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.saiful.shared.components.TMDBGallery
import com.saiful.shared.model.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    images: List<Image>,
    startIndex: Int,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Gallery") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        TMDBGallery(
            images = images.map { it.filePath },
            startIndex = startIndex,
            modifier = Modifier.padding(padding)
        )
    }
}
