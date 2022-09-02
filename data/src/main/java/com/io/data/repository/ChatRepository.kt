package com.io.data.repository

import com.io.data.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val messagesFLow: Flow<List<MessageDTO>>

}