package com.saiful.tvshows.view.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.shared.model.TvShowsCategory
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBImageSlider
import com.saiful.shared.components.TMDBTvShowItem
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.SliderItem

@Composable
fun TvShowsDashboardScreen(
    viewModel: ShowsDashboardVM,
    onShowClick: (Int) -> Unit,
    onSeeAllClick: (TvShowsCategory) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    TvShowsDashboardContent(
        uiState = uiState,
        onShowClick = onShowClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
fun TvShowsDashboardContent(
    uiState: DashboardUiState,
    onShowClick: (Int) -> Unit,
    onSeeAllClick: (TvShowsCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        if (uiState.sliderShows.isNotEmpty()) {
            TMDBImageSlider(
                items = uiState.sliderShows.map { 
                    SliderItem(it.id, it.name, it.backdropPath)
                },
                onItemClick = onShowClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TvShowSection(
            title = "Trending",
            shows = uiState.trendingShows,
            onShowClick = onShowClick,
            onSeeAllClick = { onSeeAllClick(TvShowsCategory.TRENDING) }
        )

        TvShowSection(
            title = "Popular",
            shows = uiState.popularShows,
            onShowClick = onShowClick,
            onSeeAllClick = { onSeeAllClick(TvShowsCategory.POPULAR) }
        )

        TvShowSection(
            title = "Top Rated",
            shows = uiState.topRatedShows,
            onShowClick = onShowClick,
            onSeeAllClick = { onSeeAllClick(TvShowsCategory.TOP_RATED) }
        )

        TvShowSection(
            title = "On Air",
            shows = uiState.onAirShows,
            onShowClick = onShowClick,
            onSeeAllClick = { onSeeAllClick(TvShowsCategory.ON_AIR) }
        )
    }
}

@Composable
private fun TvShowSection(
    title: String,
    shows: List<TvShows>,
    onShowClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column {
        TMDBSectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick
        )
        TMDBHorizontalList(
            items = shows,
            itemContent = { show ->
                TMDBTvShowItem(
                    tvShow = show,
                    onClick = onShowClick
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowSectionPreview() {
    val dummyShow = TvShows(
        id = 1,
        name = "The Last of Us",
        posterPath = null,
        voteAverage = 8.8
    )
    TMDBTheme {
        TvShowSection(
            title = "Trending Shows",
            shows = listOf(dummyShow, dummyShow, dummyShow),
            onShowClick = {},
            onSeeAllClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TvShowsDashboardContentPreview() {
    val dummyShow = TvShows(
        id = 1,
        name = "The Last of Us",
        posterPath = null,
        backdropPath = null,
        voteAverage = 8.8
    )
    TMDBTheme {
        TvShowsDashboardContent(
            uiState = DashboardUiState(
                trendingShows = listOf(dummyShow, dummyShow),
                popularShows = listOf(dummyShow, dummyShow),
                topRatedShows = listOf(dummyShow),
                onAirShows = listOf(dummyShow),
                sliderShows = listOf(dummyShow, dummyShow)
            ),
            onShowClick = {},
            onSeeAllClick = {}
        )
    }
}
