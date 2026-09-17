package com.saiful.person.view.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.data.repository.paging.list.ListRepo
import com.saiful.shared.model.PersonCategory
import com.saiful.shared.model.Person
import com.saiful.shared.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PersonListVM @Inject constructor(
    private val repo: ListRepo,
    private val apiService: PersonApiService,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val personList: Flow<PagingData<Person>>

    init {
        val categoryName = savedStateHandle.toRoute<Route.PersonList>().category
        val category = PersonCategory.valueOf(categoryName)
        val service = when (category) {
            PersonCategory.POPULAR -> apiService::popularPersons
            PersonCategory.TRENDING -> apiService::trendingPersons
        }
        personList = repo.getPersonPager(service).cachedIn(viewModelScope)
    }

    fun selectedCategory(category: PersonCategory) {
        // No longer needed
    }
}