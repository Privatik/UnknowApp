package com.io.unknow.presentation.chat.model

import com.io.domain.model.MessageDO
import com.io.domain.model.MessageDTO

data class MessageUI(
    val id: String,
    val userId: String,
    val userName: String,
    val message: String,
    val time: Long
)

fun MessageDTO.asUI(): MessageUI{
    return MessageUI(
        id = this.id,
        userId = this.userId,
        userName = this.userName,
        message = text,
        time = this.timeSend
    )
}