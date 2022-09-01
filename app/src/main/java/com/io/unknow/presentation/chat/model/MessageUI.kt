package com.io.unknow.presentation.chat.model

import com.io.domain.model.MessageDO

data class MessageUI(
    val id: String,
    val message: String,
    val time: Long
)

fun MessageDO.asUI(): MessageUI{
    return MessageUI(id, message, time)
}