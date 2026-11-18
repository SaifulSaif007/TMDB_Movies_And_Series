package com.saiful.tmdbexplorer.search

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.saiful.shared.model.search.MultiSearchResult
import com.saiful.shared.utils.AppConstants
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.CrimsonBright
import com.saiful.base.ui.theme.CrimsonLight
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.OutlineVariantDark
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerHighestDark
import com.saiful.base.ui.theme.SurfaceContainerLowDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.base.ui.theme.TextMuted
import java.util.Locale
import com.saiful.movie.view.components.MovieListItem
import com.saiful.movie.view.search.SearchVM
import com.saiful.person.view.component.PersonListItem
import com.saiful.shared.R
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.components.TMDBSearchBar
import com.saiful.tvshows.view.component.TvShowListItem
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit,
    onPersonClick: (Int) -> Unit,
    onDiscoverClick: () -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    val movieSearchVM: SearchVM = hiltViewModel()
    val tvSearchVM: com.saiful.tvshows.view.search.SearchVM = hiltViewModel()
    val personSearchVM: com.saiful.person.view.search.SearchVM = hiltViewModel()
    val multiSearchVM: MultiSearchViewModel = hiltViewModel()

    val movies = movieSearchVM.movieList.collectAsLazyPagingItems()
    val shows = tvSearchVM.showsList.collectAsLazyPagingItems()
    val persons = personSearchVM.personList.collectAsLazyPagingItems()
    val multiResults = multiSearchVM.results.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("All", "Movies", "TV Shows", "People")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- 1. Top Cinematic Header ---
        TopCinematicSearchHeader(onDiscoverClick = onDiscoverClick)

        // --- 2. Cinematic Pill Search Bar with Discover Quick Action ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TMDBSearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    movieSearchVM.searchMovie(it)
                    tvSearchVM.searchShow(it)
                    personSearchVM.searchPersons(it)
                    multiSearchVM.search(it)
                },
                onSearch = {
                    movieSearchVM.searchMovie(query)
                    tvSearchVM.searchShow(query)
                    personSearchVM.searchPersons(query)
                    multiSearchVM.search(query)
                },
                modifier = Modifier.weight(1f)
            )

            // Discover & Filter Quick Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHighDark)
                    .border(1.dp, GlassBorder, CircleShape)
                    .clickable { onDiscoverClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Discover & Filter",
                    tint = CinemaCrimson,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // --- 3. Segmented Category Tabs Switcher ---
        SearchCategoryTabs(
            tabs = tabs,
            selectedIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            }
        )

        // --- 4. Content Pager ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> {
                    SearchPageContent(
                        pagingItems = multiResults,
                        query = query,
                        emptyState = {
                            AllEmptyState(
                                isSearching = query.isNotBlank(),
                                query = query,
                                onDiscoverClick = onDiscoverClick
                            )
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(multiResults.itemCount) { index ->
                                val item = multiResults[index]
                                if (item != null) {
                                    MultiSearchResultItem(
                                        item = item,
                                        onClick = {
                                            when (item.mediaType) {
                                                "movie" -> onMovieClick(item.id)
                                                "tv" -> onShowClick(item.id)
                                                "person" -> onPersonClick(item.id)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    SearchPageContent(
                        pagingItems = movies,
                        query = query,
                        emptyState = {
                            MoviesEmptyState(
                                isSearching = query.isNotBlank(),
                                query = query,
                                onDiscoverClick = onDiscoverClick
                            )
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(movies.itemCount) { index ->
                                val movie = movies[index]
                                if (movie != null) {
                                    MovieListItem(movie = movie, onClick = onMovieClick)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    SearchPageContent(
                        pagingItems = shows,
                        query = query,
                        emptyState = {
                            TvShowsEmptyState(
                                isSearching = query.isNotBlank(),
                                query = query,
                                onDiscoverClick = onDiscoverClick
                            )
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(shows.itemCount) { index ->
                                val show = shows[index]
                                if (show != null) {
                                    TvShowListItem(show = show, onClick = onShowClick)
                                }
                            }
                        }
                    }
                }

                3 -> {
                    SearchPageContent(
                        pagingItems = persons,
                        query = query,
                        emptyState = {
                            PeopleEmptyState(
                                isSearching = query.isNotBlank(),
                                query = query
                            )
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(persons.itemCount) { index ->
                                val person = persons[index]
                                if (person != null) {
                                    PersonListItem(person = person, onClick = onPersonClick)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopCinematicSearchHeader(
    onDiscoverClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Clapperboard Monogram Emblem
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(CinemaCrimson, CrimsonBright)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(16.dp)) {
                    val w = size.width
                    val h = size.height
                    drawRoundRect(
                        color = Color.White,
                        size = size,
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = Offset(w * 0.32f, h * 0.5f))
                    drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = Offset(w * 0.68f, h * 0.5f))
                }
            }

            // Wordmark
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cine",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "Wave",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = CinemaCrimson,
                        fontSize = 20.sp,
                        letterSpacing = (-0.5).sp
                    )
                )
            }

            // Vertical divider
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .width(1.dp)
                    .background(OutlineVariantDark.copy(alpha = 0.4f))
            )

            // Section Label
            Text(
                text = "Search",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceDark,
                    fontSize = 17.sp
                )
            )
        }

        // Discover Quick Action Pill
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceContainerHighDark)
                .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                .clickable { onDiscoverClick() }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Discover",
                tint = CinemaCrimson,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Discover",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceDark
                )
            )
        }
    }
}

@Composable
private fun SearchCategoryTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedIndex == index
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onTabSelected(index)
                    }
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) CinemaCrimson else TextMuted,
                        fontSize = 15.sp
                    )
                )

                // Photonic Crimson Indicator
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(3.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) CinemaCrimson else Color.Transparent)
                )
            }
        }
    }
}

@Composable
private fun SearchPageContent(
    pagingItems: LazyPagingItems<*>,
    query: String,
    emptyState: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    when {
        pagingItems.loadState.refresh is LoadState.Loading -> {
            TMDBLoadingView()
        }

        pagingItems.itemCount == 0 -> {
            emptyState()
        }

        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                if (query.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Results for",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "\"$query\"",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = OnSurfaceDark,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CinemaCrimson.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${pagingItems.itemCount} found",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CinemaCrimson,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                content()
            }
        }
    }
}

/**
 * 35mm Wave Filmstrip Empty State for Movies
 */
@Composable
private fun MoviesEmptyState(
    isSearching: Boolean,
    query: String,
    onDiscoverClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Hero Visual Container
        Box(
            modifier = Modifier
                .size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            // Ambient Glow Backing
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                CinemaCrimson.copy(alpha = pulseAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )

            // 35mm Filmstrip Wave
            Image(
                painter = painterResource(id = R.drawable.ic_movie),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    if (isSearching) TextMuted else CinemaCrimson.copy(alpha = 0.85f)
                ),
                modifier = Modifier.size(110.dp)
            )

            // Photonic Crimson Spark Accent
            if (!isSearching) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(CinemaCrimson)
                        .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearching) "No Movies Found" else "Explore Cinema & TV",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) {
                "No matches found for \"$query\". Check your spelling or try searching for another blockbuster title."
            } else {
                "Search through premier blockbusters, series, creators, and cinema history."
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                lineHeight = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp)
        )

        if (!isSearching) {
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onDiscoverClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaCrimson.copy(alpha = 0.18f),
                    contentColor = CinemaCrimson
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Discover by Genre & Streaming",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Elevated TV Card Empty State for TV Shows
 */
@Composable
private fun TvShowsEmptyState(
    isSearching: Boolean,
    query: String,
    onDiscoverClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Hero Visual Container
        Box(
            modifier = Modifier
                .size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            // Ambient Glow Backing
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                CinemaCrimson.copy(alpha = pulseAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Elevated TV Glass Card
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(SurfaceContainerHighDark.copy(alpha = 0.75f))
                    .border(1.dp, GlassBorder, RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                // TV Screen Icon
                Image(
                    painter = painterResource(id = R.drawable.ic_show),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        if (isSearching) TextMuted else CinemaCrimson
                    ),
                    modifier = Modifier.size(54.dp)
                )

                // Signal Pulse Micro Dot
                if (!isSearching) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 16.dp, end = 16.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CinemaCrimson)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearching) "No TV Shows Found" else "Discover Television & Series",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) {
                "No series matches found for \"$query\". Check your spelling or try another show title."
            } else {
                "Search across trending series, network originals, anime, and episode guides."
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                lineHeight = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp)
        )

        if (!isSearching) {
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onDiscoverClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaCrimson.copy(alpha = 0.18f),
                    contentColor = CinemaCrimson
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Discover by Genre & Streaming",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Concentric Spotlight Theater Mask Empty State for People
 */
@Composable
private fun PeopleEmptyState(
    isSearching: Boolean,
    query: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.24f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Hero Visual Container
        Box(
            modifier = Modifier
                .size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            // Ambient Spotlight Cone & Glow
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                CinemaCrimson.copy(alpha = pulseAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Concentric Spotlight Ring Outer
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerLowDark.copy(alpha = 0.5f))
                    .border(1.dp, GlassBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Concentric Spotlight Ring Middle
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerDark.copy(alpha = 0.7f))
                        .border(1.dp, GlassBorder, CircleShape)
                )
            }

            // Center Hero Badge
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHighestDark)
                    .border(1.dp, GlassBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        if (isSearching) TextMuted else CinemaCrimson
                    ),
                    modifier = Modifier.size(36.dp)
                )
            }

            // Floating Star Award Chip (Right-Top)
            if (!isSearching) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 26.dp, end = 26.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerHighDark)
                        .border(1.dp, GlassBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = NeonAmber,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearching) "No People Found" else "Find Actors & Filmmakers",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) {
                "No creative talents found for \"$query\". Check your spelling or try another name."
            } else {
                "Search actors, directors, writers, and award-winning creative talents."
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                lineHeight = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp)
        )
    }
}

@Composable
fun MultiSearchResultItem(
    item: MultiSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    val imageShape = RoundedCornerShape(12.dp)
    val title = item.displayTitle
    val overview = item.overview
    val voteAverage = item.voteAverage
    val releaseYear = item.displayDate?.take(4)
    val mediaType = item.mediaType
    val knownFor = item.knownForDepartment

    val imageUrl = when (mediaType) {
        "person" -> {
            if (!item.profilePath.isNullOrBlank()) {
                AppConstants.IMAGE_BASE_URL + AppConstants.PROFILE_SIZE + item.profilePath
            } else null
        }
        else -> {
            if (!item.posterPath.isNullOrBlank()) {
                AppConstants.IMAGE_BASE_URL + AppConstants.POSTER_SIZE + item.posterPath
            } else null
        }
    }

    val (badgeText, badgeColor) = when (mediaType) {
        "movie" -> "Movie" to CinemaCrimson
        "tv" -> "TV Show" to Color(0xFF29B6F6)
        "person" -> "Person" to NeonAmber
        else -> mediaType.replaceFirstChar { it.uppercase() } to TextMuted
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SurfaceContainerDark)
            .border(1.dp, GlassBorder, cardShape)
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(imageShape)
                    .background(SurfaceContainerLowestDark)
                    .border(1.dp, GlassBorder, imageShape),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(
                            id = when (mediaType) {
                                "movie" -> R.drawable.ic_movie
                                "tv" -> R.drawable.ic_show
                                else -> R.drawable.ic_person
                            }
                        ),
                        contentDescription = null,
                        tint = TextMuted.copy(alpha = 0.5f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Details Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .padding(vertical = 4.dp, horizontal = 2.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceDark,
                                fontSize = 16.sp,
                                letterSpacing = (-0.3).sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Media Type Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeColor.copy(alpha = 0.15f))
                                .border(1.dp, badgeColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = badgeColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    if (!overview.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = overview,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else if (!knownFor.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Known for: $knownFor",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                // Rating & Metadata Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (voteAverage != null && voteAverage > 0.0) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHighDark)
                                .border(1.dp, GlassBorder, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = NeonAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = String.format(Locale.US, "%.1f", voteAverage),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "/ 10",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    if (!releaseYear.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CinemaCrimson.copy(alpha = 0.15f))
                                .border(1.dp, CinemaCrimson.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = releaseYear,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CrimsonLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Unified Multi-Search Empty State
 */
@Composable
private fun AllEmptyState(
    isSearching: Boolean,
    query: String,
    onDiscoverClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                CinemaCrimson.copy(alpha = pulseAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceContainerHighDark.copy(alpha = 0.75f))
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_movie),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(CinemaCrimson),
                        modifier = Modifier.size(28.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_show),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color(0xFF29B6F6)),
                        modifier = Modifier.size(28.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(NeonAmber),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isSearching) "No Matches Found" else "Search Everything",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = OnSurfaceDark
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) {
                "No movies, series, or people matched \"$query\". Check your spelling or try another search term."
            } else {
                "Search across movies, TV series, actors, and directors simultaneously."
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                lineHeight = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 280.dp)
        )

        if (!isSearching) {
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onDiscoverClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaCrimson.copy(alpha = 0.18f),
                    contentColor = CinemaCrimson
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Discover by Genre & Streaming",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllEmptyStatePreview() {
    TMDBTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianCanvas)
        ) {
            AllEmptyState(isSearching = false, query = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MoviesEmptyStatePreview() {
    TMDBTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianCanvas)
        ) {
            MoviesEmptyState(isSearching = false, query = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowsEmptyStatePreview() {
    TMDBTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianCanvas)
        ) {
            TvShowsEmptyState(isSearching = false, query = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PeopleEmptyStatePreview() {
    TMDBTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianCanvas)
        ) {
            PeopleEmptyState(isSearching = false, query = "")
        }
    }
}
