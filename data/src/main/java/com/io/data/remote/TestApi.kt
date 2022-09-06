package com.io.data.remote

import android.util.Log
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.client.statement.*

private const val baseApi = "http://192.168.1.4:9001"

interface TestApi {

    suspend fun doRequest(): Result<HttpResponse>
}

class TestApiImpl(
    private val client: HttpClient
): TestApi{
    override suspend fun doRequest(): Result<HttpResponse> = client.requestAsResult{
       client.get<HttpResponse>("$baseApi/api/")
    }

}