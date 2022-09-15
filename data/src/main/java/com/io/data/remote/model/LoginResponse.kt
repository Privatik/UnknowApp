package com.io.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val tokens: TokenResponse,
    val user: UserResponse
)

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)


