package com.io.data.websocket

import android.util.Log
import com.io.data.remote.MessageApi
import com.io.data.remote.MessageResponse
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive

interface WebSocketManager<T> {
    val data: Flow<T>
}

class WebSocketManagerImpl(
    private val messageApi: MessageApi
) : WebSocketManager<Result<List<MessageResponse>>>{
    private val _data = MutableSharedFlow<Result<List<MessageResponse>>>(replay = 1)
    override val data: Flow<Result<List<MessageResponse>>> = flow {
        while (currentCoroutineContext().isActive){
            Log.d("Socket", "connect")
            messageApi.socketMessage()
                .collect{
                    emit(it.transform())
                }
        }
    }

    private suspend fun MessageApi.ResponseFromMessageSocket.transform(): Result<List<MessageResponse>>{
        Log.d("Socket","trams $this")
        return when(this){
            MessageApi.ResponseFromMessageSocket.DontAuth -> {
                messageApi.getMessages(0, 20).map {
                    it.message
                }
            }
            is MessageApi.ResponseFromMessageSocket.NewMessage -> {
                messageApi.getMessage(this.id).map {
                    listOf(it.message)
                }
            }
            MessageApi.ResponseFromMessageSocket.Exit -> {
                Result.success(emptyList())
            }
        }
    }

    private fun Flow<MessageApi.ResponseFromMessageSocket>.transform(): Flow<Result<List<MessageResponse>>>{
        return map { apiAnswer ->
            Log.d("Socket","trams $apiAnswer")
            when(apiAnswer){
                MessageApi.ResponseFromMessageSocket.DontAuth -> {
                    val newResult = messageApi.getMessages(0, 20).map {
                        it.message
                    }
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