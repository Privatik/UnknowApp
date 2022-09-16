package com.io.domain.model

data class MessageDTO(
    val id: String,
    val userId: String,
    val userName: String,
    val timeSend: Long,
    val text: String
)
