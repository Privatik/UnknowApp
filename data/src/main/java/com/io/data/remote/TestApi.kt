package com.io.data.remote

import com.io.data.remote.model.LoginResponse
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface TestApi {

    suspend fun doRequest(
        email: String,
        password: String,
        nickName: String
    ): Result<ResponseBody<LoginResponse>>

    suspend fun checkValid(): Result<HttpResponse>
}

class TestApiImpl(
    private val client: HttpClient,
    private val baseApi: String
): TestApi{
    override suspend fun doRequest(
        email: String,
        password: String,
        nickName: String
    ): Result<ResponseBody<LoginResponse>> = client.requestAndConvertToResult(
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

    override suspend fun checkValid(): Result<HttpResponse> = client.requestAndConvertToResult(
        urlString = "$baseApi/api/valid",
        method = HttpMethod.Get,
    )

}