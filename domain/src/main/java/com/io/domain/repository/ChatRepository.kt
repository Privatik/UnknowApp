package com.io.domain.repository

import com.io.domain.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val messagesFLow: Flow<List<MessageDTO>>

    suspend fun sendMessage(text: String): Result<Boolean>
}