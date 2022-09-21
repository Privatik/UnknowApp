package com.io.data.repository

import android.util.Log
import com.io.data.di.DataServiceLocator
import com.io.data.paging.MessagePagination
import com.io.data.remote.MessageApi
import com.io.data.remote.MessageResponse
import com.io.data.remote.ResponseBody
import com.io.data.remote.implMessageApi
import com.io.data.storage.DataStorage
import com.io.data.websocket.WebSocketManager
import com.io.data.websocket.WebSocketManagerImpl
import com.io.domain.model.MessageDTO
import com.io.domain.repository.ChatRepository
import io.pagination.common.PagingAdapter
import kotlinx.coroutines.flow.*
import java.util.concurrent.CancellationException
import kotlin.collections.map
import kotlin.map

fun implChatRepository(): ChatRepository{
    val instance = DataServiceLocator.instance()
    return ChatRepositoryImpl(instance.dataStorage, implMessageApi())
}

class ChatRepositoryImpl(
    private val dataStore: DataStorage,
    private val messageApi: MessageApi,
    private val paginator: PagingAdapter<Int, Result<List<MessageResponse>>> = PagingAdapter(MessagePagination(messageApi)),
    private val webSocketManager: WebSocketManager<Result<List<MessageResponse>>> = WebSocketManagerImpl(messageApi)
): ChatRepository {

    override val messagesFLow: Flow<Result<List<MessageDTO>>> =
        merge(
            webSocketManager.data,
            paginator.data { it }
        )
        .map { result ->
            Log.d("Socket","Map to dto")
            result.map { response ->
                response.map { it.asDTO() }
            }
        }

    override suspend fun sendMessage(text: String) {
        messageApi.send(text)
    }

    override suspend fun refreshPage(initPage: Int) = paginator.actionRefresh(initPage)
    override suspend fun actionLoadNextPage() = paginator.actionLoadNext()
    override suspend fun actionLoadPreviousPage() = paginator.actionLoadPrevious()

}