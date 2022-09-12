package com.io.data.remote

import com.io.data.remote.model.LogicResponse
import com.io.data.remote.model.RefreshRequest
import com.io.data.remote.model.TokenResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*

private const val baseApi = "http://10.0.2.2:9000"

interface TestApi {

    suspend fun doRequest(
        email: String,
        password: String,
        nickName: String
    ): Result<ResponseBody<LogicResponse>>

    suspend fun refresh(userid: String, refreshToken: String): Result<ResponseBody<TokenResponse>>

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

    override suspend fun refresh(userid: String, refreshToken: String): Result<ResponseBody<TokenResponse>> = client.requestAndConvertToResult(
        urlString = "$baseApi/api/refresh_token",
        method = HttpMethod.Post
    ){
        header("Authorization","Bearer $refreshToken")
        body = RefreshRequest(userid)
    }

    override suspend fun checkValid(accessToken: String): Result<HttpResponse> = client.requestAndConvertToResult(
        urlString = "$baseApi/api/valid",
        method = HttpMethod.Get
    ) {
        header("Authorization","Bearer $accessToken")
    }

}