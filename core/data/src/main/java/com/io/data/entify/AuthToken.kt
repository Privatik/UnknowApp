package com.io.data.entify

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun empty() = AuthToken(
            accessToken = "",
            refreshToken = ""
        )
    }
}
