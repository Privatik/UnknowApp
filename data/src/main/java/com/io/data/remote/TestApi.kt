package com.io.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

private const val baseApi = "http://192.168.1.4:9001"

interface TestApi {

    suspend fun doRequest(): Result<String>
}

class TestApiImpl(
    private val client: HttpClient
): TestApi{
    override suspend fun doRequest(): Result<String> = client.requestAndConvertToResult(
        urlString = "$baseApi/api/",
        method = HttpMethod.Get
    ){

    }

}