package com.io.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userId: Flow<String>
    suspend fun register(email: String, userName: String, password: String): Result<Boolean>
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun logout(): Result<Boolean>
}