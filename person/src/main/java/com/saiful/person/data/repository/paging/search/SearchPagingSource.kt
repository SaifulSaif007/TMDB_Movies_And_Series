package com.saiful.person.data.repository.paging.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.person.data.api.PersonApiService
import com.saiful.person.model.Person

class SearchPagingSource(private val apiService: PersonApiService, private val query: String) :
    PagingSource<Int, Person>() {

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val data = apiService.searchPerson(query, pageCount)) {
            is BaseResponse.Success -> {
                val response = data.body.results
                createPage(response, pageCount)
            }

            else -> LoadResult.Error(Exception("No person found"))
        }
    }

    private fun createPage(persons: List<Person>, pageCount: Int) =
        LoadResult.Page(
            data = persons,
            prevKey = if (pageCount == START_PAGE_INDEX) null else pageCount - 1,
            nextKey = if (persons.isEmpty()) null else pageCount + 1
        )

    companion object {
        const val START_PAGE_INDEX = 1
    }
}