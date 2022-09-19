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
    return MessageApiImpl(instance.client, instance.clientWebsocket)
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
        userId: String,
        userName: String,
        text: String
    ): Result<ResponseBody<MessageResponse>>

    fun socketMessage(): Flow<ResponseFromMessageSocket>

    suspend fun getMessage(id: String): Result<ResponseBody<MessageResponse>>

    suspend fun getMessages(page: Int, pageSize: Int): Result<ResponseBody<List<MessageResponse>>>
}

internal class MessageApiImpl(
    private val client: HttpClient,
    private val clientWebsocket: HttpClient
): MessageApi {
    private val instance = DataServiceLocator.instance()

    override suspend fun send(
        userId: String,
        userName: String,
        text: String
    ): Result<ResponseBody<MessageResponse>> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/api/message/send",
        method = HttpMethod.Post
    ) {
        body = MessageRequest(userId, userName, text)
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
                                when (get(0)){
                                    MessageApi.ResponseFromMessageSocket.NewMessage.TAG -> {
                                        emit(MessageApi.ResponseFromMessageSocket.NewMessage(get(1)))
                                    }
                                }
                            }
                        }
                        is Frame.Close -> {
                            if (answer.readReason()?.code == CloseReason.Codes.CANNOT_ACCEPT.code){
                                Log.d("Socket", "auth error")
                                emit(MessageApi.ResponseFromMessageSocket.DontAuth)
                            }
                        }
                        is Frame.Binary,
                        is Frame.Ping,
                        is Frame.Pong -> {
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                    if (closeReason.await()?.code == CloseReason.Codes.CANNOT_ACCEPT.code){
                        emit(MessageApi.ResponseFromMessageSocket.DontAuth)
                    } else {
                        emit(MessageApi.ResponseFromMessageSocket.Exit)
                    }
                    Log.d("Socket", e.toString())
                } catch (e: ConnectTimeoutException) {
                    Log.d("Socket", e.toString())
                    emit(MessageApi.ResponseFromMessageSocket.Exit)
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