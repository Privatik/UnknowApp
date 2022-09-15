package com.io.domain.repository

interface UserRepository {
    suspend fun register(email: String, userName: String, password: String): Result<Boolean>
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun logout(): Result<Boolean>
}