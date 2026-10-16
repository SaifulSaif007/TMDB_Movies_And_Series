package com.saiful.person.view.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.saiful.person.view.component.PersonListItem
import com.saiful.shared.components.TMDBErrorView
import com.saiful.shared.components.TMDBLoadingView
import com.saiful.shared.model.Person
import com.saiful.shared.model.PersonCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListScreen(
    category: PersonCategory,
    viewModel: PersonListVM,
    onBackClick: () -> Unit,
    onPersonClick: (Int) -> Unit
) {
    val persons = viewModel.personList.collectAsLazyPagingItems()

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
        PersonListContent(
            modifier = Modifier.padding(padding),
            persons = persons,
            onPersonClick = onPersonClick
        )
    }
}

@Composable
fun PersonListContent(
    persons: LazyPagingItems<Person>,
    onPersonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = persons.itemCount,
                key = persons.itemKey { it.id },
                contentType = persons.itemContentType { "person" }
            ) { index ->
                persons[index]?.let { person ->
                    PersonListItem(person = person, onClick = onPersonClick)
                }
            }

            when (val state = persons.loadState.append) {
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

        when (val state = persons.loadState.refresh) {
            is LoadState.Loading -> {
                TMDBLoadingView()
            }
            is LoadState.Error -> {
                TMDBErrorView(message = state.error.message ?: "Error loading persons")
            }
            else -> {}
        }
    }
}

