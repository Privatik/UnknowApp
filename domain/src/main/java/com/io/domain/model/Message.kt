package com.io.domain.model

data class Message(
    val id: String,
    val message: MessageType,
    val time: Long
)

sealed class MessageType{
    data class TextType(val text: String): MessageType()
    data class PictureType(val url: String): MessageType()
}
