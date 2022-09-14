package com.io.data.remote

import android.util.Log
import com.io.data.token.TokenManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

enum class Token{
    NONE,
    ACCESS,
    REFRESH
}

suspend inline fun <reified T> HttpClient.requestAndConvertToResult(
    urlString: String,
    method: HttpMethod,
    attributes: Map<AttributeKey<Any>, Any> = emptyMap(),
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

fun HttpRequestBuilder.updateJWTToken(token: String){
    headers.remove(HttpHeaders.Authorization)
    header(HttpHeaders.Authorization, "Bearer $token")
}

@KtorExperimentalAPI
public class JWTToken(
) {
    public var tokenManager: TokenManager<HttpClient>? = null
    public var urlEncodedPathWithOutToken: Set<String> = setOf()
    public var urlEncodedPathWithRefreshToken: Set<String> = setOf()

    public companion object Feature : HttpClientFeature< JWTToken, JWTToken> {
        override val key: AttributeKey<JWTToken> = AttributeKey("JWTToken")

        override fun prepare(block: JWTToken.() -> Unit): JWTToken =
            JWTToken().apply(block)

        override fun install(feature: JWTToken, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                if (feature.tokenManager == null) return@intercept

                val endPath = context.url.encodedPath
                Log.d("Ktor","Add token")
                when {
                    feature.urlEncodedPathWithOutToken.contains(endPath) -> {
                        Log.d("Ktor","withOut token")
                    }
                    feature.urlEncodedPathWithRefreshToken.contains(endPath) -> {
                        context.updateJWTToken(feature.tokenManager?.getRefreshToken() ?: "")
                        Log.d("Ktor","refresh token")
                    }
                    else -> {
                        context.updateJWTToken(feature.tokenManager?.getAccessToken() ?: "")
                        Log.d("Ktor","access token")
                    }
                }
            }

            scope.requestPipeline.intercept(HttpRequestPipeline.Send) { content ->
                feature.tokenManager?.let { manager ->
                    val sender = DefaultSender(scope)
                    Log.d("Ktor","Start request $content")
                    val call = if (content is HttpClientCall) content else sender.execute(context)

                    if (call.response.status.value == 401) {
                        val newAccessToken = manager.updateToken(
                            call.client!!,
                            context.headers[HttpHeaders.Authorization]
                        ) ?: return@let

                        context.updateJWTToken(newAccessToken)
                        Log.d("Ktor","Send refresh call")
                        proceedWith(sender.execute(context))
                    } else {
                        Log.d("Ktor","Send call")
                        proceedWith(call)
                    }
                }
            }
        }

        private class DefaultSender(
            private val client: HttpClient
        ): Sender{
            override suspend fun execute(requestBuilder: HttpRequestBuilder): HttpClientCall {
                val sendResult = client.sendPipeline.execute(requestBuilder, requestBuilder.body)

                return sendResult as? HttpClientCall
                    ?: error("Failed to execute send pipeline. Expected to got [HttpClientCall], but received $sendResult")
            }

        }
    }
}