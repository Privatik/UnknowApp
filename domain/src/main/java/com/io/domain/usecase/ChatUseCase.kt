package com.io.domain.usecase

import com.io.domain.model.Message
import kotlinx.coroutines.flow.MutableSharedFlow


class ChatUseCase {
    val messages = MutableSharedFlow<Message>()

    suspend operator fun invoke(message: Message){

    }
}