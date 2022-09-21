package com.io.data.remote

import android.util.Log
import com.io.data.di.DataServiceLocator
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

fun implMessageApi(): MessageApi{
    val instance = DataServiceLocator.instance()
    return MessageApiImpl(instance.client)
}

interface MessageApi {

    sealed class ResponseFromMessageSocket{
        data class NewMessage(val id: String): ResponseFromMessageSocket(){
            companion object{
                val TAG: String = "NewMessage"
            }
        }
        object DontAuth: ResponseFromMessageSocket()
        object Exit: ResponseFromMessageSocket()
    }

    suspend fun send(
        text: String
    ): Result<ResponseBody<MessageResponse>>

    fun socketMessage(): Flow<ResponseFromMessageSocket>

    suspend fun getMessage(id: String): Result<ResponseBody<MessageResponse>>

    suspend fun getMessages(page: Int, pageSize: Int): Result<ResponseBody<List<MessageResponse>>>
}

internal class MessageApiImpl(
    private val client: HttpClient,
): MessageApi {
    private val instance = DataServiceLocator.instance()

    override suspend fun send(
        text: String
    ): Result<ResponseBody<MessageResponse>> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/api/message/send",
        method = HttpMethod.Post
    ) {
        body = SendMessageRequest(text)
    }

    override fun socketMessage(): Flow<MessageApi.ResponseFromMessageSocket> = flow {
        client.webSocket(
            method = HttpMethod.Get,
            host = instance.host,
            port = instance.port,
            path = "/chat/message"
        ) {
            while (isActive) {
                try {
                    when (val answer = incoming.receive()) {
                        is Frame.Text -> {
                            val text = answer.readText()
                            Log.d("Socket", "get $text")
                            text.split(":").apply {
                                if (getOrNull(0) == MessageApi.ResponseFromMessageSocket.NewMessage.TAG){
                                    Log.d("Socket", "emit")
                                    emit(MessageApi.ResponseFromMessageSocket.NewMessage(get(1)))
                                }
                            }
                        }
                        is Frame.Close -> {
                            close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT,""))
                        }
                        is Frame.Binary,
                        is Frame.Ping,
                        is Frame.Pong -> Unit
                    }
                } catch (e: ClosedReceiveChannelException) {
                    if (closeReason.await()?.code == CloseReason.Codes.CANNOT_ACCEPT.code){
                        Log.d("Socket", "dont auth $e")
                        emit(MessageApi.ResponseFromMessageSocket.DontAuth)
                    } else {
                        Log.d("Socket", "global $e")
                        emit(MessageApi.ResponseFromMessageSocket.Exit)
                    }
                } catch (e: Exception) {
                    Log.d("Socket", e.toString())
                    emit(MessageApi.ResponseFromMessageSocket.Exit)
                    throw e
                }
            }
        }
    }

    override suspend fun getMessage(
        id: String
    ): Result<ResponseBody<MessageResponse>> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/api/message",
        method = HttpMethod.Get
    ) {
        url.parameters.append("id",id)
    }

    override suspend fun getMessages(
        page: Int,
        pageSize: Int
    ): Result<ResponseBody<List<MessageResponse>>> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/api/messages",
        method = HttpMethod.Get
    ) {
        url.parameters.append("page", page.toString())
        url.parameters.append("pageSize", pageSize.toString())
    }

}