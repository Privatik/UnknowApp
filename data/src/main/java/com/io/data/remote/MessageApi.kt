package com.io.data.remote

import com.io.data.di.DataServiceLocator
import com.io.data.remote.model.LoginRequest
import io.ktor.client.*
import io.ktor.http.*

fun implMessageApi(): MessageApi{
    val instance = DataServiceLocator.instance()
    return MessageApiImpl(instance.client)
}

interface MessageApi {

    suspend fun send(
        userId: String,
        userName: String,
        text: String
    ): Result<ResponseBody<MessageResponse>>

    suspend fun getMessage(id: String): Result<ResponseBody<MessageResponse>>
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
        urlString = "${instance.baseApi}/message/send",
        method = HttpMethod.Post
    ) {
        body = MessageRequest(userId, userName, text)
    }

    override suspend fun getMessage(
        id: String
    ): Result<ResponseBody<MessageResponse>> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/messages",
        method = HttpMethod.Get
    ) {
        url.parameters.append("id",id)
    }

}