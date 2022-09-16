package com.io.data.remote

import com.io.domain.model.MessageDTO
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val id: String,
    val userId: String,
    val userName: String,
    val text: String,
    val timeSend: Long
) {
    fun asDTO(): MessageDTO =
        MessageDTO(
            id = this.id,
            userId = this.userId,
            userName = this.userName,
            text = this.text,
            timeSend = this.timeSend
        )
}
