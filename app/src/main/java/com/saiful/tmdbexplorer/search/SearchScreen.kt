package com.saiful.tmdbexplorer.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.saiful.movie.view.components.MovieListItem
import com.saiful.movie.view.search.SearchVM
import com.saiful.person.view.component.PersonListItem
import com.saiful.shared.R
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.components.TMDBSearchBar
import com.saiful.tvshows.view.component.TvShowListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    onMovieClick: (Int) -> Unit,
    onShowClick: (Int) -> Unit,
    onPersonClick: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val movieSearchVM: SearchVM = hiltViewModel()
    val tvSearchVM: com.saiful.tvshows.view.search.SearchVM = hiltViewModel()
    val personSearchVM: com.saiful.person.view.search.SearchVM = hiltViewModel()

    val movies = movieSearchVM.movieList.collectAsLazyPagingItems()
    val shows = tvSearchVM.showsList.collectAsLazyPagingItems()
    val persons = personSearchVM.personList.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Movies", "TV Shows", "People")

    Column(modifier = Modifier.fillMaxSize()) {
        TMDBSearchBar(
            query = query,
            onQueryChange = {
                query = it
                movieSearchVM.searchMovie(it)
                tvSearchVM.searchShow(it)
                personSearchVM.searchPersons(it)
            },
            onSearch = { active = false },
            active = active,
            onActiveChange = { active = it }
        )

        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> {
                    SearchPageContent(
                        pagingItems = movies,
                        emptyImageRes = R.drawable.ic_movie
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
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

                1 -> {
                    SearchPageContent(
                        pagingItems = shows,
                        emptyImageRes = R.drawable.ic_show
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
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

                2 -> {
                    SearchPageContent(
                        pagingItems = persons,
                        emptyImageRes = R.drawable.ic_person
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
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
private fun SearchPageContent(
    pagingItems: LazyPagingItems<*>,
    emptyImageRes: Int,
    content: @Composable () -> Unit
) {
    when {
        pagingItems.loadState.refresh is LoadState.Loading -> {
            TMDBLoadingView()
        }

        pagingItems.itemCount == 0 -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = emptyImageRes),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        else -> {
            content()
        }
    }
}
