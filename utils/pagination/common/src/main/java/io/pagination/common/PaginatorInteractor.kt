package io.pagination.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class PaginatorInteractor<Key: Any, B: Any>(
    private val worker: Paginator<Key, B>,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val data: Flow<Result<B>> = worker.data
    @Volatile private var keyBody: KeyBody<Key>? = null
    @Volatile private var isLoading: Boolean = false

    suspend fun actionRefresh(initPage: Key) = loading {
        keyBody = worker.refreshPage(initPage)
    }

    suspend fun actionLoadNext() = loading{
        keyBody?.nextPage?.let { key ->
            keyBody = keyBody?.copy(nextPage = worker.nextPage(key))
        }
    }

    suspend fun actionLoadPrevious() = loading{
        keyBody?.lastPage?.let { key ->
            keyBody = keyBody?.copy(lastPage =  worker.previousPage(key))
        }
    }

    private suspend inline fun loading(
        crossinline go: suspend () -> Unit
    ) = withContext(dispatcher){
        if (isLoading){
            return@withContext
        }
        isLoading = true
        go()
        isLoading = false
    }

}
