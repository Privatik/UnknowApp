package com.io.data.internal.repository

import com.io.domain.model.User
import com.io.domain.repository.LoginRepository
import com.io.domain.state.Sex
import javax.inject.Inject

class LoginRepositoryImpl: LoginRepository {

    override suspend fun register(userName: String, email: String, password: String): User {
        return User(
            id = "",
            email = "",
            sex =  Sex.MAN,
            birthDay = 1000L
        )
    }

    override suspend fun logIn(email: String, password: String): User {
        return User(
            id = "",
            email = "",
            sex =  Sex.MAN,
            birthDay = 1000L
        )
    }
}