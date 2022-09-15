package com.io.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val id: String,
    val userId: String,
    val userName: String,
    val text: String,
    val timeSend: Long
)
