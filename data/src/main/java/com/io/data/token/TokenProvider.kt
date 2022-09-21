package com.io.data.token

import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope

interface TokenProvider {
    suspend fun updateToken(token: String?)
    suspend fun getToken(): String?
}