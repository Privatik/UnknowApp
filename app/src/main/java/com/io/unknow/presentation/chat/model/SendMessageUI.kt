package com.io.unknow.presentation.chat.model

import com.io.domain.model.SendMessageDO

data class SendMessageUI(
    val text: String
)

fun SendMessageUI.asDo(id: String): SendMessageDO{
    return SendMessageDO(id, text)
}