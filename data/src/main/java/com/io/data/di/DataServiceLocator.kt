package com.io.data.di

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.io.data.encrypted.CryptoManager
import com.io.data.remote.JWTToken
import com.io.data.storage.userPreferencesDataStore
import com.io.data.token.JWTAccessTokenProvider
import com.io.data.token.JWTRefreshTokenProvider
import com.io.data.token.JWTTokenManager
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*

class DataServiceLocator private constructor(
    private val context: Context
){
    val baseApi = "http://10.0.2.2:9000"

    companion object {
        private var service: DataServiceLocator? = null

        fun instance(context: Context): DataServiceLocator{
            if (service == null){
                service = DataServiceLocator(context.applicationContext)
            }
            return service!!
        }
    }

    @KtorExperimentalAPI
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

            install(JWTToken){
                tokenManager = jwtTokenManager
                urlEncodedPathWithOutToken = setOf(
                    "/api/user/create"
                )
                urlEncodedPathWithRefreshToken = setOf(
                    "/api/refresh_token"
                )
            }

            install(HttpSend) {
                intercept { httpClientCall, httpRequestBuilder ->
                    Log.d("Ktor","start httpsSend load ${httpRequestBuilder.body}")
                    httpClientCall
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

            install(DefaultRequest) {
                if (!(body is FormDataContent || body is MultiPartFormDataContent)) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            }
        }
    }

    private val dataStore = context.userPreferencesDataStore
    private val accessTokenProvider = JWTAccessTokenProvider()
    private val refreshTokenProvider = JWTRefreshTokenProvider(dataStore, CryptoManager())

    val jwtTokenManager = JWTTokenManager(
        baseApi,
        accessTokenProvider,
        refreshTokenProvider,
        dataStore
    )
}