package com.io.data.di

import android.util.Log
import io.ktor.client.*
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

object DataServiceLocator {

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
                    builder.headers
                    call
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
                Log.d("Logger Ktor =>", "Default")
                if (!(body is FormDataContent || body is MultiPartFormDataContent)) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            }
        }
    }
}