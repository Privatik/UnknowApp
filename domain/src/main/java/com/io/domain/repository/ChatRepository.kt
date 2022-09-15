package com.io.domain.repository

import com.io.domain.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val messagesFLow: Flow<List<MessageDTO>>

    fun sendMessage(text: String): Result<Boolean>

    fun logOut(): Result<Boolean>
}