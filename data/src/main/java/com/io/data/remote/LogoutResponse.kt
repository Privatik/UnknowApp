package com.io.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val isSuccessful: Boolean
)