package com.saiful.person.view.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saiful.base.ui.theme.TMDBTheme
import com.saiful.person.model.PersonCategory
import com.saiful.shared.components.TMDBHorizontalList
import com.saiful.shared.components.TMDBPersonItem
import com.saiful.shared.components.TMDBSectionHeader
import com.saiful.shared.model.Person

@Composable
fun PersonDashboardScreen(
    viewModel: PersonDashboardVM,
    onPersonClick: (Int) -> Unit,
    onSeeAllClick: (PersonCategory) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    PersonDashboardContent(
        uiState = uiState,
        onPersonClick = onPersonClick,
        onSeeAllClick = onSeeAllClick
    )
}

@Composable
fun PersonDashboardContent(
    uiState: PersonDashboardUiState,
    onPersonClick: (Int) -> Unit,
    onSeeAllClick: (PersonCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        PersonSection(
            title = "Popular People",
            persons = uiState.popularPersons,
            onPersonClick = onPersonClick,
            onSeeAllClick = { onSeeAllClick(PersonCategory.POPULAR) }
        )

        PersonSection(
            title = "Trending People",
            persons = uiState.trendingPersons,
            onPersonClick = onPersonClick,
            onSeeAllClick = { onSeeAllClick(PersonCategory.TRENDING) }
        )
    }
}

@Composable
private fun PersonSection(
    title: String,
    persons: List<Person>,
    onPersonClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit
) {
    if (persons.isEmpty()) return

    Column {
        TMDBSectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick
        )
        
        // First row
        TMDBHorizontalList(
            items = if (persons.size >= 10) persons.subList(0, 10) else persons,
            itemContent = { person ->
                TMDBPersonItem(
                    person = person,
                    onClick = onPersonClick
                )
            }
        )

        // Second row if we have enough items
        if (persons.size > 10) {
            TMDBHorizontalList(
                items = if (persons.size >= 20) persons.subList(10, 20) else persons.subList(10, persons.size),
                itemContent = { person ->
                    TMDBPersonItem(
                        person = person,
                        onClick = onPersonClick
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonDashboardContentPreview() {
    val dummyPerson = Person(
        id = 1,
        name = "Tom Cruise",
        popularity = 99.0,
        profilePath = null
    )
    TMDBTheme {
        PersonDashboardContent(
            uiState = PersonDashboardUiState(
                popularPersons = List(15) { dummyPerson },
                trendingPersons = List(5) { dummyPerson }
            ),
            onPersonClick = {},
            onSeeAllClick = {}
        )
    }
}
