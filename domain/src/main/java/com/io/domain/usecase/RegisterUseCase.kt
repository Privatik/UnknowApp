package com.io.domain.usecase

class RegisterUseCase {

    suspend operator fun invoke(
        userName: String,
        password: String
    ): Result<Boolean> {
        return Result.success(true)
    }
}