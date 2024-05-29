package com.saiful.person.view.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.data.repository.paging.list.ListRepo
import com.saiful.person.model.Person
import com.saiful.person.model.PersonCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PersonListVM @Inject constructor(
    private val repo: ListRepo,
    private val apiService: PersonApiService
) : BaseViewModel() {

    private var service = apiService::popularPersons
    private lateinit var selectedCat: String
    lateinit var personList: Flow<PagingData<Person>>

    fun selectedCategory(category: PersonCategory) {
        if (this::selectedCat.isInitialized) {
            return
        }
        selectedCat = category.value
        service = when (category) {
            PersonCategory.POPULAR -> apiService::popularPersons
            PersonCategory.TRENDING -> apiService::trendingPersons
        }

        persons()
    }

    private fun persons() {
        personList = repo.getPersonPager(service).cachedIn(viewModelScope)
    }
}