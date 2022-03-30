package com.io.domain.usecase

import com.io.domain.model.User
import com.io.domain.repository.LoginRepository
import com.io.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.*

class AuthorizationUseCase(
    private val repository: LoginRepository
) {

    operator fun invoke(
        email: String,
        password: String,
        userName: String? = null,
        isRegister: Boolean = false
    ): Flow<Resource<User>> = flow{
        try {
            emit(Resource.Loading)
            val user = if (isRegister){
                repository.register(userName!!, email, password)
            } else {
                repository.logIn(email, password)
            }
            emit(Resource.Success<User>(user))
        } catch (e: IOException){
            emit(Resource.Error(e))
        }
    }
}