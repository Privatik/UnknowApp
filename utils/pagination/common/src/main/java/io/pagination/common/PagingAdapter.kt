package io.pagination.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

open class PagingAdapter<Key: Any, T: Any>(
    private val worker: Pager<Key, T>,
) {
    @Volatile private var keyBody: KeyBody<Key>? = null
    @Volatile private var isLoading: Boolean = false

    fun <B: Any> data(mapper: suspend (T) -> B): Flow<B>{
        return worker.data.map(mapper)
    }

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
    ) {
        if (isLoading){
            return
        }
        isLoading = true
        go()
        isLoading = false
    }

}
