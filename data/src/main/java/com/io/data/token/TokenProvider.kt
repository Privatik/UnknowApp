package com.io.data.token

import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope

interface TokenProvider: CoroutineScope {
    suspend fun updateToken(token: String?)
    suspend fun getToken(): String?
}