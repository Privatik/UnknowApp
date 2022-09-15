package com.io.domain.usecase

import com.io.domain.repository.UserRepository

class RegisterUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        email: String,
        userName: String,
        password: String
    ): Result<Boolean> {
        return userRepository.register(email, userName, password)
    }
}