package com.io.data.di

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.io.data.encrypted.CryptoManager
import com.io.data.remote.CustomDefaultRequest
import com.io.data.remote.Token
import com.io.data.remote.model.RefreshRequest
import com.io.data.remote.model.TokenResponse
import com.io.data.remote.requestAndConvertToResult
import com.io.data.remote.updateJWTToken
import com.io.data.storage.userPreferencesDataStore
import com.io.data.token.JWTAccessTokenProvider
import com.io.data.token.JWTRefreshTokenProvider
import com.io.data.token.JWTTokenManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

@SuppressLint("StaticFieldLeak")
object DataServiceLocator {
    var context: Context? = null
    val baseApi = "http://10.0.2.2:9000"

    @OptIn(KtorExperimentalAPI::class)
    val client by lazy {
        HttpClient(CIO) {
            expectSuccess = true

            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(HttpSend){
                intercept { call, builder ->
                    if (call.response.status.value == 401){
                        val newAccessToken = jwtTokenManager.updateToken(
                            call.client!!,
                            builder.headers[HttpHeaders.Authorization]
                        ) ?: return@intercept call
                        builder.updateJWTToken(newAccessToken)
                        execute(builder)
                    } else {
                        call
                    }
                }
            }


            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Logger Ktor =>", message)
                    }

                }
                level = LogLevel.ALL
            }

            install(CustomDefaultRequest) {
                modification { builder ->
                    builder.apply {
                        if (!(body is FormDataContent || body is MultiPartFormDataContent)) {
                            header(HttpHeaders.ContentType, ContentType.Application.Json)
                        }
                        when (attributes.getOrNull(AttributeKey<Token>("Token"))){
                            Token.ACCESS -> {
                                updateJWTToken(accessTokenProvider.getToken()!!)
                            }
                            Token.REFRESH -> {
                                updateJWTToken(refreshTokenProvider.getToken()!!)
                            }
                            Token.NONE,
                            null -> {}
                        }
                    }
                }
            }
        }
    }

    private val dataStore = context!!.userPreferencesDataStore
    private val accessTokenProvider = JWTAccessTokenProvider()
    private val refreshTokenProvider = JWTRefreshTokenProvider(dataStore, CryptoManager())

    private val jwtTokenManager = JWTTokenManager(
        accessTokenProvider,
        refreshTokenProvider,
        dataStore
    )
}