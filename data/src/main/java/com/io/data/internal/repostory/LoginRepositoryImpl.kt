package com.io.data.internal.repostory

import com.io.data.api.datasource.LoginDataSource
import com.io.data.internal.mapper.toModel
import com.io.data.internal.model.LoginRequest
import com.io.domain.model.User
import com.io.domain.repository.LoginRepository

internal class LoginRepositoryImpl(
    private val dataSource: LoginDataSource
): LoginRepository {

    override suspend fun logIn(email: String, password: String): User {
        val res = dataSource.login(
            LoginRequest(email, password)
        )

        return res.user.toModel()
    }
}