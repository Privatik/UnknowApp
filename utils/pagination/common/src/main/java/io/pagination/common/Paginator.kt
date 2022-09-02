package io.pagination.common

import kotlinx.coroutines.flow.Flow

interface Paginator<Key: Any, B: Any>{
    val data: Flow<Result<B>>

    suspend fun refreshPage(initPage: Key): KeyBody<Key>

    suspend fun nextPage(pagingBody: Key): Key?

    suspend fun previousPage(pagingBody: Key): Key?

}