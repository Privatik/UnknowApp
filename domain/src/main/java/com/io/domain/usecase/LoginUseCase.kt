package com.io.domain.usecase

import com.io.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Boolean> {
        return userRepository.login(email, password)
    }
}