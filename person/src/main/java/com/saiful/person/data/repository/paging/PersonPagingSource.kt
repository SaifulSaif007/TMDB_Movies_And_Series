package com.saiful.person.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saiful.base.network.model.BaseResponse
import com.saiful.base.network.model.GenericResponse
import com.saiful.person.model.Person
import com.saiful.person.model.PersonResponse

class PersonPagingSource(private val apiCall: suspend (page: Int) -> GenericResponse<PersonResponse>) :
    PagingSource<Int, Person>() {

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val pageCount = params.key ?: START_PAGE_INDEX

        return when (val data = apiCall.invoke(pageCount)) {
            is BaseResponse.Success -> {
                val response = data.body.results
                createPage(response, pageCount)
            }
            else -> {
                LoadResult.Error(Exception("No Person found"))
            }
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