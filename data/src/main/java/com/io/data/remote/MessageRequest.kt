package com.io.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    val userId: String,
    val userName: String,
    val text: String
)