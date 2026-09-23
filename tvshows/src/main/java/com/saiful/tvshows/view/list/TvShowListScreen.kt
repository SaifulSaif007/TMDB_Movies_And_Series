package com.saiful.tvshows.view.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.saiful.shared.components.TMDBErrorView
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.TvShowsCategory
import com.saiful.shared.utils.AppConstants
import com.saiful.shared.utils.floatNumberFormatter
import com.saiful.tvshows.view.component.TvShowListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowListScreen(
    category: TvShowsCategory,
    viewModel: ListVM,
    onBackClick: () -> Unit,
    onShowClick: (Int) -> Unit
) {
    val shows = viewModel.showsList.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category.value) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        TvShowListContent(
            modifier = Modifier.padding(padding),
            shows = shows,
            onShowClick = onShowClick
        )
    }
}

@Composable
fun TvShowListContent(
    shows: LazyPagingItems<TvShows>,
    onShowClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = shows.itemCount,
                key = shows.itemKey { it.id },
                contentType = shows.itemContentType { "show" }
            ) { index ->
                shows[index]?.let { show ->
                    TvShowListItem(show = show, onClick = onShowClick)
                }
            }

            when (val state = shows.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        TMDBErrorView(message = state.error.message ?: "Error loading more")
                    }
                }

                else -> {}
            }
        }

        when (val state = shows.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView()
            }

            is LoadState.Error -> {
                TMDBErrorView(message = state.error.message ?: "Error loading shows")
            }

            else -> {}
        }
    }
}


