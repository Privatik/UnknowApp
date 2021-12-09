package com.io.data.api.datasource

import com.io.data.internal.model.LoginRequest
import com.io.data.internal.model.LoginResponse

interface LoginDataSource {

    suspend fun login(loginModel: LoginRequest): LoginResponse
}