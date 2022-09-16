package com.io.domain.usecase

import com.io.domain.model.MessageDTO
import com.io.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ChatInteractor(
    private val chatRepository: ChatRepository
) {
    val messagesFLow: Flow<List<MessageDTO>> = chatRepository.messagesFLow

    suspend fun send(text: String): Result<Boolean>{
        return chatRepository.sendMessage(text)
    }
}