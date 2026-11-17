package com.saiful.tmdbexplorer.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TextMuted
import com.saiful.movie.view.components.MovieListItem
import com.saiful.shared.components.TMDBErrorView
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.WatchProviderItem
import com.saiful.shared.model.account.AccountMediaTab
import com.saiful.shared.model.discover.DiscoverFilter
import com.saiful.shared.model.discover.DiscoverSortBy
import com.saiful.shared.model.discover.GenreItem
import com.saiful.shared.utils.AppConstants
import com.saiful.tvshows.view.component.TvShowListItem
import kotlinx.coroutines.launch

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit
) {
    val filter by viewModel.filter.collectAsState()
    val movieGenres by viewModel.movieGenres.collectAsState()
    val tvGenres by viewModel.tvGenres.collectAsState()
    val watchProviders by viewModel.watchProviders.collectAsState()

    val movies = viewModel.discoverMovies.collectAsLazyPagingItems()
    val shows = viewModel.discoverTvShows.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(
        initialPage = filter.mediaTab.ordinal,
        pageCount = { AccountMediaTab.entries.size }
    )
    val coroutineScope = rememberCoroutineScope()
    var showFilterSheet by remember { mutableStateOf(false) }

    // Sync pager swipe to ViewModel
    LaunchedEffect(pagerState.currentPage) {
        val tab = AccountMediaTab.entries[pagerState.currentPage]
        if (filter.mediaTab != tab) {
            viewModel.selectMediaTab(tab)
        }
    }

    // Sync ViewModel mediaTab to Pager
    LaunchedEffect(filter.mediaTab) {
        if (pagerState.currentPage != filter.mediaTab.ordinal) {
            pagerState.animateScrollToPage(filter.mediaTab.ordinal)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- 1. Sticky Cinematic Top App Bar ---
        DiscoverTopBar(
            title = "Discover & Filter",
            filter = filter,
            onBackClick = onBackClick,
            onResetClick = { viewModel.resetFilters() }
        )

        // --- 2. Media Tabs (Movies vs TV Shows) ---
        DiscoverTabRow(
            selectedTabIndex = pagerState.currentPage,
            onTabSelected = { tab ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(tab.ordinal)
                }
            }
        )

        // --- 3. Filter Quick-Chips Bar ---
        DiscoverFilterStrip(
            filter = filter,
            onOpenFilterSheet = { showFilterSheet = true },
            onClearGenre = { viewModel.selectGenre(null) },
            onClearYear = { viewModel.selectYear(null) },
            onClearProvider = { viewModel.selectProvider(null) }
        )

        // --- 4. Swipeable Discover Pager Content ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (AccountMediaTab.entries[page]) {
                AccountMediaTab.MOVIES -> {
                    DiscoverMoviesContent(
                        movies = movies,
                        filter = filter,
                        onMovieClick = onMovieClick,
                        onResetFilters = { viewModel.resetFilters() }
                    )
                }
                AccountMediaTab.TV_SHOWS -> {
                    DiscoverTvShowsContent(
                        shows = shows,
                        filter = filter,
                        onShowClick = onShowClick,
                        onResetFilters = { viewModel.resetFilters() }
                    )
                }
            }
        }
    }

    // --- 5. Interactive Filter Bottom Sheet ---
    if (showFilterSheet) {
        val currentGenres = if (filter.mediaTab == AccountMediaTab.MOVIES) movieGenres else tvGenres
        DiscoverFilterBottomSheet(
            filter = filter,
            genres = currentGenres,
            years = viewModel.availableYears,
            providers = watchProviders,
            onDismiss = { showFilterSheet = false },
            onSelectSortBy = { viewModel.selectSortBy(it) },
            onSelectGenre = { viewModel.selectGenre(it) },
            onSelectYear = { viewModel.selectYear(it) },
            onSelectProvider = { viewModel.selectProvider(it) },
            onResetFilters = { viewModel.resetFilters() }
        )
    }
}

@Composable
private fun DiscoverTopBar(
    title: String,
    filter: DiscoverFilter,
    onBackClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
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
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Title with Accent Emblem
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = CinemaCrimson,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurfaceDark,
                            fontSize = 20.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Reset Action Button
            if (!filter.isDefault) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CinemaCrimson.copy(alpha = 0.15f))
                        .clickable { onResetClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Filters",
                        tint = CinemaCrimson,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CinemaCrimson
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GlassBorder)
        )
    }
}

@Composable
private fun DiscoverTabRow(
    selectedTabIndex: Int,
    onTabSelected: (AccountMediaTab) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = SurfaceContainerLowestDark,
        contentColor = CinemaCrimson,
        indicator = { tabPositions ->
            if (selectedTabIndex in tabPositions.indices) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = CinemaCrimson,
                    height = 3.dp
                )
            }
        },
        divider = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(GlassBorder)
            )
        }
    ) {
        AccountMediaTab.entries.forEachIndexed { index, tab ->
            val isSelected = index == selectedTabIndex
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        ),
                        color = if (isSelected) CinemaCrimson else TextMuted
                    )
                }
            )
        }
    }
}

@Composable
private fun DiscoverFilterStrip(
    filter: DiscoverFilter,
    onOpenFilterSheet: () -> Unit,
    onClearGenre: () -> Unit,
    onClearYear: () -> Unit,
    onClearProvider: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowestDark)
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "All Filters" Master Button with Count Badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (filter.activeFilterCount > 0) CinemaCrimson else SurfaceContainerHighDark)
                .border(1.dp, if (filter.activeFilterCount > 0) CinemaCrimson else GlassBorder, RoundedCornerShape(10.dp))
                .clickable { onOpenFilterSheet() }
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filters",
                tint = if (filter.activeFilterCount > 0) Color.White else OnSurfaceDark,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = if (filter.activeFilterCount > 0) "Filters (${filter.activeFilterCount})" else "Filters",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (filter.activeFilterCount > 0) Color.White else OnSurfaceDark
                )
            )
        }

        // Sort Chip
        ActiveFilterChip(
            label = filter.sortBy.title,
            isActive = filter.sortBy != DiscoverSortBy.POPULARITY_DESC,
            onClick = onOpenFilterSheet
        )

        // Genre Chip
        val genre = filter.genre
        if (genre != null) {
            ActiveFilterChip(
                label = genre.name,
                isActive = true,
                onClear = onClearGenre
            )
        } else {
            ActiveFilterChip(
                label = "+ Genre",
                isActive = false,
                onClick = onOpenFilterSheet
            )
        }

        // Year Chip
        val year = filter.year
        if (year != null) {
            ActiveFilterChip(
                label = year.toString(),
                isActive = true,
                onClear = onClearYear
            )
        } else {
            ActiveFilterChip(
                label = "+ Year",
                isActive = false,
                onClick = onOpenFilterSheet
            )
        }

        // Provider Chip
        val provider = filter.provider
        if (provider != null) {
            ActiveFilterChip(
                label = provider.providerName ?: "Streaming",
                isActive = true,
                onClear = onClearProvider
            )
        } else {
            ActiveFilterChip(
                label = "+ Streaming",
                isActive = false,
                onClick = onOpenFilterSheet
            )
        }
    }
}

@Composable
private fun ActiveFilterChip(
    label: String,
    isActive: Boolean,
    onClick: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isActive) CinemaCrimson.copy(alpha = 0.2f) else SurfaceContainerHighDark)
            .border(1.dp, if (isActive) CinemaCrimson else GlassBorder, RoundedCornerShape(10.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive) CinemaCrimson else TextMuted
            )
        )
        if (onClear != null) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear $label",
                tint = CinemaCrimson,
                modifier = Modifier
                    .size(14.dp)
                    .clickable { onClear() }
            )
        }
    }
}

@Composable
private fun DiscoverMoviesContent(
    movies: LazyPagingItems<Movies>,
    filter: DiscoverFilter,
    onMovieClick: (Int) -> Unit,
    onResetFilters: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        when (val state = movies.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView(modifier = Modifier.fillMaxSize())
            }
            is LoadState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TMDBErrorView(message = state.error.message ?: "Failed to discover movies")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { movies.retry() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Retry", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            is LoadState.NotLoading -> {
                if (movies.itemCount == 0) {
                    EmptyDiscoverState(
                        mediaType = "movies",
                        filter = filter,
                        onResetFilters = onResetFilters
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            count = movies.itemCount,
                            key = movies.itemKey { it.id },
                            contentType = movies.itemContentType { "movie" }
                        ) { index ->
                            movies[index]?.let { movie ->
                                MovieListItem(movie = movie, onClick = onMovieClick)
                            }
                        }

                        when (val appendState = movies.loadState.append) {
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
                                            modifier = Modifier.size(30.dp)
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
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Button(
                                            onClick = { movies.retry() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = CinemaCrimson,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(text = "Retry", fontSize = 12.sp)
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
private fun DiscoverTvShowsContent(
    shows: LazyPagingItems<TvShows>,
    filter: DiscoverFilter,
    onShowClick: (Int) -> Unit,
    onResetFilters: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        when (val state = shows.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView(modifier = Modifier.fillMaxSize())
            }
            is LoadState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TMDBErrorView(message = state.error.message ?: "Failed to discover TV shows")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { shows.retry() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaCrimson,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Retry", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            is LoadState.NotLoading -> {
                if (shows.itemCount == 0) {
                    EmptyDiscoverState(
                        mediaType = "TV shows",
                        filter = filter,
                        onResetFilters = onResetFilters
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            count = shows.itemCount,
                            key = shows.itemKey { it.id },
                            contentType = shows.itemContentType { "tvshow" }
                        ) { index ->
                            shows[index]?.let { show ->
                                TvShowListItem(show = show, onClick = onShowClick)
                            }
                        }

                        when (val appendState = shows.loadState.append) {
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
                                            modifier = Modifier.size(30.dp)
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
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Button(
                                            onClick = { shows.retry() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = CinemaCrimson,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(text = "Retry", fontSize = 12.sp)
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
private fun EmptyDiscoverState(
    mediaType: String,
    filter: DiscoverFilter,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(CinemaCrimson.copy(alpha = 0.25f), Color.Transparent)
                    )
                )
                .border(1.5.dp, CinemaCrimson.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = CinemaCrimson,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "No $mediaType Match Your Filters",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark,
                fontSize = 18.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Try clearing or relaxing some of your filters (genre, year, or streaming provider) to see more results.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                fontSize = 13.sp,
                lineHeight = 19.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onResetFilters,
            colors = ButtonDefaults.buttonColors(
                containerColor = CinemaCrimson,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Reset All Filters",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun DiscoverFilterBottomSheet(
    filter: DiscoverFilter,
    genres: List<GenreItem>,
    years: List<Int>,
    providers: List<WatchProviderItem>,
    onDismiss: () -> Unit,
    onSelectSortBy: (DiscoverSortBy) -> Unit,
    onSelectGenre: (GenreItem?) -> Unit,
    onSelectYear: (Int?) -> Unit,
    onSelectProvider: (WatchProviderItem?) -> Unit,
    onResetFilters: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceContainerLowestDark,
        contentColor = OnSurfaceDark,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(TextMuted.copy(alpha = 0.5f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Refine & Filter",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceDark,
                        fontSize = 20.sp
                    )
                )

                if (!filter.isDefault) {
                    Text(
                        text = "Reset All",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CinemaCrimson
                        ),
                        modifier = Modifier
                            .clickable { onResetFilters() }
                            .padding(4.dp)
                    )
                }
            }

            // 1. Sort By Section
            SectionHeader(title = "Sort By")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                DiscoverSortBy.entries.forEach { sortBy ->
                    val isSelected = filter.sortBy == sortBy
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectSortBy(sortBy) },
                        label = { Text(sortBy.title) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CinemaCrimson,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceContainerHighDark,
                            labelColor = OnSurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = GlassBorder,
                            selectedBorderColor = CinemaCrimson
                        )
                    )
                }
            }

            // 2. Genres Section
            SectionHeader(title = "Genre")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                genres.forEach { genre ->
                    val isSelected = filter.genre?.id == genre.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectGenre(genre) },
                        label = { Text(genre.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonAmber,
                            selectedLabelColor = ObsidianCanvas,
                            containerColor = SurfaceContainerHighDark,
                            labelColor = OnSurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = GlassBorder,
                            selectedBorderColor = NeonAmber
                        )
                    )
                }
            }

            // 3. Release Year Section
            SectionHeader(title = "Release Year")
            val yearScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(yearScrollState)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                years.forEach { year ->
                    val isSelected = filter.year == year
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectYear(year) },
                        label = { Text(year.toString()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CinemaCrimson,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceContainerHighDark,
                            labelColor = OnSurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = GlassBorder,
                            selectedBorderColor = CinemaCrimson
                        )
                    )
                }
            }

            // 4. Streaming Providers Section
            SectionHeader(title = "Streaming On (US)")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                providers.forEach { provider ->
                    val isSelected = filter.provider?.providerId == provider.providerId
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectProvider(provider) },
                        leadingIcon = {
                            if (!provider.logoPath.isNullOrBlank()) {
                                AsyncImage(
                                    model = AppConstants.IMAGE_BASE_URL + "w92" + provider.logoPath,
                                    contentDescription = provider.providerName,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        },
                        label = { Text(provider.providerName ?: "") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CinemaCrimson,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceContainerHighDark,
                            labelColor = OnSurfaceDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = GlassBorder,
                            selectedBorderColor = CinemaCrimson
                        )
                    )
                }
            }

            // Done Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaCrimson,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Apply Filters",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = OnSurfaceDark,
            fontSize = 14.sp
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
