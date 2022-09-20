package com.io.data.websocket

import android.util.Log
import com.io.data.remote.MessageApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive

interface WebSocketManager<T> {
    val data: Flow<T>

    suspend fun reconnect()
}

class WebSocketManagerImpl(
    private val messageApi: MessageApi
) : WebSocketManager<MessageApi.ResponseFromMessageSocket>{
    private val _data = MutableSharedFlow<MessageApi.ResponseFromMessageSocket>()
    override val data: Flow<MessageApi.ResponseFromMessageSocket> = merge(
        _data.asSharedFlow()
    )

    override suspend fun reconnect() {
        _data.emitAll(messageApi.socketMessage())
    }


}