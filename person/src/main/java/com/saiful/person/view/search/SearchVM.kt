package com.saiful.person.view.search

import androidx.paging.PagingData
import com.saiful.base.viewmodel.BaseViewModel
import com.saiful.person.data.repository.paging.search.SearchRepo
import com.saiful.person.model.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val searchRepo: SearchRepo
) : BaseViewModel() {

    lateinit var personList: Flow<PagingData<Person>>

    fun searchPersons(query: String) {
        personList = searchRepo.personSearchPager(query)
    }
}