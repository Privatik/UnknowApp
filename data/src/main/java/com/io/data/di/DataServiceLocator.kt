package com.io.data.di

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
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

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Logger Ktor =>", message)
                    }

                }
                level = LogLevel.ALL
            }

            install(HttpSend){
                intercept{ call, request ->
                    Log.d("Http","$request")
                    if (call.response.status.value !in 100..399) {
                        Log.d("Http","do $request")
                        request.parameter("Auth","test")
                        execute(request)
                    } else {
                        call
                    }
                }
            }


            install(DefaultRequest) {
                Log.d("Http","add default body")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}