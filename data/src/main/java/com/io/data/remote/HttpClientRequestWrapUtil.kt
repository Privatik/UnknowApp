package com.io.data.remote

import android.util.Log
import com.io.data.token.TokenManager
import com.io.domain.exception.Fail
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import java.lang.Exception

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
    } catch (e: ResponseException){
        Log.d("Ktor","$e")
        when (e.response.status.value){
            401 -> Result.failure(Fail.AuthFail())
            403 -> Result.failure(Fail.ForbiddenFail())
            else -> Result.failure(Fail.GlobalFail(e))
        }
    } catch (e: Exception){
        Log.d("Ktor","$e")
        Result.failure(Fail.GlobalFail(e))
    }
}

fun HttpRequestBuilder.updateJWTToken(token: String){
    headers.remove(HttpHeaders.Authorization)
    header(HttpHeaders.Authorization, "Bearer $token")
}

public class JWTToken() {
    public var tokenManager: TokenManager<HttpClient>? = null
    public var urlEncodedPathWithOutToken: Set<String> = setOf()
    public var urlEncodedPathWithRefreshToken: Set<String> = setOf()

    public companion object Feature : HttpClientFeature<JWTToken, JWTToken> {
        override val key: AttributeKey<JWTToken> = AttributeKey("JWTToken")

        override fun prepare(block: JWTToken.() -> Unit): JWTToken =
            JWTToken().apply(block)

        override fun install(feature: JWTToken, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                if (feature.tokenManager == null) return@intercept

                val endPath = context.url.encodedPath
                when {
                    feature.urlEncodedPathWithOutToken.contains(endPath) -> {}
                    feature.urlEncodedPathWithRefreshToken.contains(endPath) -> {
                        context.updateJWTToken(feature.tokenManager?.getRefreshToken() ?: "")
                    }
                    else -> {
                        val accessToken = feature.tokenManager?.getAccessToken() ?: kotlin.run {
                            feature.tokenManager?.updateToken(scope, "Bearer ${null}") ?: ""
                        }
                        context.updateJWTToken(accessToken)
                    }
                }
            }

            scope.feature(HttpSend)!!.intercept { call, builder ->
                feature.tokenManager?.let { manager ->
                    if (call.response.status.value == 401) {
                        val newAccessToken = manager.updateToken(
                            scope,
                            builder.headers[HttpHeaders.Authorization]
                        ) ?: return@intercept call

                        builder.updateJWTToken(newAccessToken)
                        return@intercept execute(builder)
                    }
                    call
                } ?: call
            }
        }
    }
}