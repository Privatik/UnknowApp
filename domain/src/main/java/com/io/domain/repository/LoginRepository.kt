package com.io.domain.repository

import com.io.domain.model.User

interface LoginRepository {

    suspend fun logIn(email: String, password: String): User
}