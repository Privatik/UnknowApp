package com.io.domain.usecase

class LoginUseCase {

    suspend operator fun invoke(
        userName: String,
        password: String
    ): Result<Boolean> {
        return Result.success(true)
    }
}