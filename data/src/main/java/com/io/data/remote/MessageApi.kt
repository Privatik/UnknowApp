package com.io.data.remote

import android.util.Log
import com.io.data.di.DataServiceLocator
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

fun implMessageApi(): MessageApi{
    val instance = DataServiceLocator.instance()
    return MessageApiImpl(instance.client)
}

interface MessageApi {

    sealed class AnswerFromMessageSocket(){
        data class NewMessage(val id: String): AnswerFromMessageSocket(){
            companion object{
                val TAG: String = "NewMessage"
            }
        }
        object DontAuth: AnswerFromMessageSocket()
        object Exit: AnswerFromMessageSocket()
    }

    suspend fun send(
        userId: String,
        userName: String,
        text: String
    ): Result<ResponseBody<MessageResponse>>

    fun socketMessage(): Flow<AnswerFromMessageSocket>

    suspend fun getMessage(id: String): Result<ResponseBody<MessageResponse>>

    suspend fun getMessages(page: Int, pageSize: Int): Result<ResponseBody<List<MessageResponse>>>
}

internal class MessageApiImpl(
    private val client: HttpClient
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

    override fun socketMessage(): Flow<MessageApi.AnswerFromMessageSocket> = flow {
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
                            answer.readText().split(":").apply {
                                when (get(0)){
                                    MessageApi.AnswerFromMessageSocket.NewMessage.TAG -> {
                                        emit(MessageApi.AnswerFromMessageSocket.NewMessage(get(1)))
                                    }
                                }
                            }
                        }
                        is Frame.Close -> {
                            if (answer.readReason()?.code == CloseReason.Codes.CANNOT_ACCEPT.code){
                                emit(MessageApi.AnswerFromMessageSocket.DontAuth)
                            }
                        }
                        is Frame.Binary,
                        is Frame.Ping,
                        is Frame.Pong -> {
                        }
                    }
                } catch (e: ClosedReceiveChannelException){
                    Log.d("Socket",e.toString())
                    emit(MessageApi.AnswerFromMessageSocket.Exit)
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