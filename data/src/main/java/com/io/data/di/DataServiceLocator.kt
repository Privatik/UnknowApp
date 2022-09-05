package com.io.data.di

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

@KtorExperimentalAPI
object DataServiceLocator {

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
                    val originalCall = execute(request)
                    if (originalCall.response.status.value !in 100..399) {
                        execute(request)
                    } else {
                        originalCall
                    }
                }
            }


            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}