package com.io.data.remote

import com.io.data.di.DataServiceLocator.baseApi
import com.io.data.remote.model.LogicResponse
import com.io.data.remote.model.RefreshRequest
import com.io.data.remote.model.TokenResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*

interface TestApi {

    suspend fun doRequest(
        email: String,
        password: String,
        nickName: String
    ): Result<ResponseBody<LogicResponse>>

    suspend fun checkValid(accessToken: String): Result<HttpResponse>
}

class TestApiImpl(
    private val client: HttpClient
): TestApi{
    override suspend fun doRequest(
        email: String,
        password: String,
        nickName: String
    ): Result<ResponseBody<LogicResponse>> = client.requestAndConvertToResult(
        urlString = "$baseApi/api/user/create",
        method = HttpMethod.Post
    ){
        val formParameters = formData {
            append("nickName", nickName)
            append("email", email)
            append("password", password)
        }
        body = MultiPartFormDataContent(formParameters)
    }

    override suspend fun checkValid(accessToken: String): Result<HttpResponse> = client.requestAndConvertToResult(
        urlString = "$baseApi/api/valid",
        method = HttpMethod.Get,
        attributes = mapOf(AttributeKey<Token>("Token") to Token.ACCESS)
    )

}