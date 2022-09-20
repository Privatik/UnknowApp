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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart

fun implChatRepository(): ChatRepository{
    val instance = DataServiceLocator.instance()
    return ChatRepositoryImpl(instance.dataStorage, implMessageApi())
}

class ChatRepositoryImpl(
    private val dataStore: DataStorage,
    private val messageApi: MessageApi,
    private val paginator: PagingAdapter<Int, Result<List<MessageResponse>>> = PagingAdapter(MessagePagination(messageApi)),
    private val webSocketManager: WebSocketManager<MessageApi.ResponseFromMessageSocket> = WebSocketManagerImpl(messageApi)
): ChatRepository {

    override val messagesFLow: Flow<Result<List<MessageDTO>>> =
        merge(
            webSocketManager.data.transform(),
            paginator.data { it }
        )
        .map { result ->
            Log.d("Socket","get new message ${result.getOrNull()}")
            result.map { response ->
                response.map { it.asDTO() }
            }
        }.onStart {
            webSocketManager.reconnect()
        }

    override suspend fun sendMessage(text: String) {
        messageApi.send(text)
    }

    override suspend fun refreshPage(initPage: Int) = paginator.actionRefresh(initPage)
    override suspend fun actionLoadNextPage() = paginator.actionLoadNext()
    override suspend fun actionLoadPreviousPage() = paginator.actionLoadPrevious()


    private fun Flow<MessageApi.ResponseFromMessageSocket>.transform(): Flow<Result<List<MessageResponse>>>{
        return map { apiAnswer ->
            Log.d("Socket","trams $apiAnswer")
            when(apiAnswer){
                MessageApi.ResponseFromMessageSocket.DontAuth -> {
                    val newResult = messageApi.getMessages(0, 20).map {
                        it.message
                    }
                    webSocketManager.reconnect()
                    newResult
                }
                is MessageApi.ResponseFromMessageSocket.NewMessage -> {
                    messageApi.getMessage(apiAnswer.id).map {
                        listOf(it.message)
                    }
                }
                MessageApi.ResponseFromMessageSocket.Exit -> {
                    Result.success(emptyList())
                }
            }
        }
    }

}