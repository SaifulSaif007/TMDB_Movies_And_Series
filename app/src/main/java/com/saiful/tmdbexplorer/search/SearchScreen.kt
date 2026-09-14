package com.saiful.tmdbexplorer.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.saiful.movie.view.search.SearchVM
import com.saiful.shared.components.TMDBMovieItem
import com.saiful.shared.components.TMDBPersonItem
import com.saiful.shared.components.TMDBTvShowItem
import com.saiful.shared.components.TMDBSearchBar
import com.saiful.shared.model.Movies
import com.saiful.shared.model.TvShows
import com.saiful.shared.model.Person
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (page) {
                    0 -> {
                        items(movies.itemCount) { index ->
                            val movie = movies[index]
                            if (movie != null) {
                                TMDBMovieItem(movie = movie, onClick = onMovieClick)
                            }
                        }
                    }
                    1 -> {
                        items(shows.itemCount) { index ->
                            val show = shows[index]
                            if (show != null) {
                                TMDBTvShowItem(tvShow = show, onClick = onShowClick)
                            }
                        }
                    }
                    2 -> {
                        items(persons.itemCount) { index ->
                            val person = persons[index]
                            if (person != null) {
                                TMDBPersonItem(person = person, onClick = onPersonClick)
                            }
                        }
                    }
                }
            }
        }
    }
}
