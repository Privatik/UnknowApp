package com.io.data.paging

import android.util.Log
import com.io.data.remote.MessageApi
import com.io.domain.model.MessageDTO
import io.pagination.common.KeyBody
import io.pagination.common.Paginator
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

class MessagePagination(
    private val messageApi: MessageApi
): Paginator<Int, List<MessageDTO>> {
    private val _data = MutableSharedFlow<Result<List<MessageDTO>>>()
    override val data: Flow<Result<List<MessageDTO>>>  = _data.asSharedFlow()

    override suspend fun refreshPage(initPage: Int): KeyBody<Int>  = coroutineScope{
        Log.d("Paginator","refreshPage")
        val list = List(3){ index ->
            async {
                messageApi.getMessages(index, 20)
            }
        }

        list.awaitAll()
        return@coroutineScope KeyBody(null, 3)
    }

    override suspend fun nextPage(pagingBody: Int): Int? {
        Log.d("Paginator","nextPage")
        val list = messageApi.getMessages(pagingBody, 20)
            .map { messages -> messages.message.map { it.asDTO() } }

        _data.emit(list)
        list
            .onSuccess {
                if (it.isEmpty()) { return null }
            }
            .onFailure {
                return null
            }
        return pagingBody + 1
    }

    override suspend fun previousPage(pagingBody: Int): Int? {
        return null
    }

}