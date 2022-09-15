package com.io.domain.usecase

import com.io.domain.repository.ChatRepository

class ChatInteractor(
    private val chatRepository: ChatRepository
) {

    suspend fun send(text: String): Result<Boolean>{
        return chatRepository.sendMessage(text)
    }

    suspend fun logout(): Result<Boolean>{
        return chatRepository.logOut()
    }
}