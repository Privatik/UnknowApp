package com.io.domain.repository

import com.io.domain.model.User

interface LoginRepository {

    suspend fun register(userName: String, email: String, password: String): User
    suspend fun logIn(email: String, password: String): User
}