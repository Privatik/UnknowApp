package com.io.data.remote

import io.ktor.client.*
import io.ktor.client.request.*

suspend inline fun <reified T> HttpClient.requestAsResult(
    block: HttpClient.() -> T
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Throwable){
        Result.failure(e)
    }
}