package com.saiful.person.view.list

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
import com.saiful.shared.model.Person
import com.saiful.shared.model.PersonCategory
import com.saiful.shared.utils.AppConstants

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

@Composable
private fun PersonListItem(
    person: Person,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(person.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            AsyncImage(
                model = AppConstants.IMAGE_BASE_URL + AppConstants.PROFILE_SIZE + person.profilePath,
                contentDescription = person.name,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = person.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = person.knownForDepartment ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = person.knownFor?.joinToString(", ") { it.title ?: it.name ?: "" } ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
