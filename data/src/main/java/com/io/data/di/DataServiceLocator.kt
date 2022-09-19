package com.io.data.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.io.data.encrypted.CryptoManager
import com.io.data.remote.JWTToken
import com.io.data.storage.DataStorage
import com.io.data.storage.DataStorageImpl
import com.io.data.storage.userPreferencesDataStore
import com.io.data.token.JWTAccessTokenProvider
import com.io.data.token.JWTRefreshTokenProvider
import com.io.data.token.JWTTokenManager
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import java.lang.NullPointerException

class DataServiceLocator private constructor(
    val context: Context
){
    val host = "10.0.2.2"
    val port = 9000
    val baseApi = "http://$host:$port"

    companion object {
        private var service: DataServiceLocator? = null

        fun instance(): DataServiceLocator{
            if (service == null){
                throw NullPointerException()
            }
            return service!!
        }

        fun instance(context: Context): DataServiceLocator{
            if (service == null){
                service = DataServiceLocator(context.applicationContext)
            }
            return service!!
        }
    }

    val clientWebsocket by lazy {
        HttpClient(CIO) {
            install(WebSockets) {
                pingInterval = 20_000
            }

//            myJWTToken()

//            install(Logging) {
//                logger = object : Logger {
//                    override fun log(message: String) {
//                        Log.d("Logger Ktor WebSocket =>", message)
//                    }
//
//                }
//                level = LogLevel.ALL
//            }
        }

    }

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

            install(WebSockets) {
                pingInterval = 20_000
            }

            myJWTToken()

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Logger Ktor =>", message)
                    }

                }
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                if (!(body is FormDataContent || body is MultiPartFormDataContent)) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            }
        }
    }

    fun HttpClientConfig<CIOEngineConfig>.myJWTToken(){
        install(JWTToken){
            tokenManager = jwtTokenManager
            urlEncodedPathWithOutToken = setOf(
                "/api/user/create",
                "/api/login"
            )
            urlEncodedPathWithRefreshToken = setOf(
                "/api/refresh_token"
            )
        }
    }

    private val cryptoManager = CryptoManager()
    private val dataStore = context.userPreferencesDataStore
    val dataStorage = DataStorageImpl(dataStore, cryptoManager)
    private val accessTokenProvider = JWTAccessTokenProvider()
    private val refreshTokenProvider = JWTRefreshTokenProvider(dataStore, cryptoManager)

    val jwtTokenManager = JWTTokenManager(
        baseApi,
        accessTokenProvider,
        refreshTokenProvider,
        dataStorage
    )
}