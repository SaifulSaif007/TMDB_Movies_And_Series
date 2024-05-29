package com.saiful.person.data.repository.paging.list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.saiful.base.network.model.GenericResponse
import com.saiful.person.model.PersonResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepo
@Inject constructor() {

    fun getPersonPager(apiCall: suspend (page: Int) -> GenericResponse<PersonResponse>) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ListPagingSource(apiCall) }
    ).flow
}