package com.io.data.token

import io.ktor.http.*

interface TokenProvider {
    suspend fun updateToken(token: String?)
    suspend fun getToken(): String?
}