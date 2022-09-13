package com.io.data.remote

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.Identity.encode
import io.ktor.utils.io.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlin.reflect.typeOf

enum class Token{
    NONE,
    ACCESS,
    REFRESH
}

suspend inline fun <reified T, A: Any> HttpClient.requestAndConvertToResult(
    urlString: String,
    method: HttpMethod,
    attributes: Map<AttributeKey<A>, A> = emptyMap(),
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> {
    return try {
        val response = request<T> {
            url.takeFrom(urlString)
            this.method = method
            attributes.forEach { (key, value) -> this.attributes.put(key, value) }
            block()
        }
        Result.success(response)
    } catch (e: Throwable){
        Result.failure(e)
    }
}

suspend inline fun <reified T> HttpClient.requestAndConvertToResult(
    urlString: String,
    method: HttpMethod,
    block: HttpRequestBuilder.() -> Unit = {}
): Result<T> {
    return try {
        val response = request<T> {
            url.takeFrom(urlString)
            this.method = method
            block()
        }
        Result.success(response)
    } catch (e: Throwable){
        Result.failure(e)
    }
}

fun HttpRequestBuilder.updateJWTToken(token: String){
    headers.remove(HttpHeaders.Authorization)
    header(HttpHeaders.Authorization, "Bearer $token")
}

public class CustomDefaultRequest() {
    private var taskModification: (suspend (HttpRequestBuilder) -> Unit)? = null

    fun modification(block: suspend (HttpRequestBuilder) -> Unit){
        taskModification = block
    }

    public companion object Feature : HttpClientFeature<CustomDefaultRequest, CustomDefaultRequest> {
        override val key: AttributeKey<CustomDefaultRequest> = AttributeKey("CustomDefaultRequest")

        override fun prepare(block: CustomDefaultRequest.() -> Unit): CustomDefaultRequest =
            CustomDefaultRequest().apply(block)

        override fun install(feature: CustomDefaultRequest, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                feature.taskModification?.let { block -> block(context) }
            }
        }
    }
}