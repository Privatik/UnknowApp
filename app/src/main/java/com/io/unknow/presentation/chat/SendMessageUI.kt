package com.io.unknow.presentation.chat

import com.io.domain.model.SendMessageDO

data class SendMessageUI(
    val text: String
)

fun SendMessageUI.asDo(id: String): SendMessageDO{
    return SendMessageDO(id, text)
}